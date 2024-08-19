package com.example.project1.security;

import com.example.project1.Service.impl.JwtGeneratorImpl;
import com.example.project1.Service.impl.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter implements JwtRequestProcessor{

	private JwtGeneratorImpl jwtGenerator;
	private UserDetailsService userDetailsService;
	private TokenBlacklistService tokenBlacklistService;


	@Autowired
	public JwtRequestFilter(JwtGeneratorImpl jwtGenerator,
							UserDetailsService userDetailsService,
							TokenBlacklistService tokenBlacklistService) {
		this.jwtGenerator = jwtGenerator;
		this.userDetailsService = userDetailsService;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		processJwtRequest(request, response, filterChain);
	}

	@Override
	public void processJwtRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		//final
		String requestTokenHeader = request.getHeader("Authorization");

		String jwtToken = null;
		String username = request.getParameter("username");

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);

			try {
				if (tokenBlacklistService.isTokenBlacklisted(jwtToken)) {
					logger.warn("JWT Token is blacklisted");
					filterChain.doFilter(request, response);
					return;
				}

				if (jwtGenerator.validateToken(jwtToken)) {
					username = jwtGenerator.getUsernameFromToken(jwtToken);

					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UserDetails userDetails = userDetailsService.loadUserByUsername(username);

						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} else {
					logger.warn("JWT Token validation failed");
				}
			} catch (Exception e) {
				logger.error("Exception during JWT Token validation", e);
			}
		} else {
			logger.warn("Authorization header is missing or invalid");
		}

		filterChain.doFilter(request, response);
	}
	/*@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String jwtToken = null;
		String username = request.getParameter("username");

		// 检查 Authorization 是否存在且格式正确
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);

			try {
				// 驗證 Token 是否在黑名单中
				if (tokenBlacklistService.isTokenBlacklisted(jwtToken)) {
					logger.warn("JWT Token is blacklisted");
					filterChain.doFilter(request, response);
					return;
				}

				// 驗證 Token
				if (jwtGenerator.validateToken(jwtToken)) {
					// 从 Token 中提取用户名
					username = jwtGenerator.getUsernameFromToken(jwtToken);

					// 如果用户名存在且當前没有認證，則進行身份驗證
					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UserDetails userDetails = userDetailsService.loadUserByUsername(username);


						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} else {
					logger.warn("JWT Token validation failed");
				}
			} catch (Exception e) {
				logger.error("Exception during JWT Token validation", e);
			}
		} else {
			logger.warn("Authorization header is missing or invalid");
		}

		filterChain.doFilter(request, response);
	}*/
}
