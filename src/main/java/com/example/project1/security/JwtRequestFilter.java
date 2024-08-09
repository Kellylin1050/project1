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
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtGeneratorImpl jwtGenerator;
	private final UserDetailsService userDetailsService;
	private final TokenBlacklistService tokenBlacklistService; // 新增：用于 Token 黑名单检查

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
	}
}
	/*private final JwtGeneratorImpl jwtGenerator;
	private final UserDetailsService userDetailsService;

	@Autowired
	public JwtRequestFilter(JwtGeneratorImpl jwtGenerator, UserDetailsService userDetailsService) {
		this.jwtGenerator = jwtGenerator;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String jwtToken = null;
		String username = null;

		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);

			try {
				// Validate token
				if (jwtGenerator.validateToken(jwtToken)) {
					// Extract username from the token
					username = jwtGenerator.getUsernameFromToken(jwtToken);

					// Set the authentication in the context
					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UserDetails userDetails = userDetailsService.loadUserByUsername(username);

						// Create and set authentication token
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			} catch (Exception e) {
				logger.warn("JWT Token validation failed", e);
			}
		} else {
			logger.warn("Authorization header is missing or invalid");
		}

		filterChain.doFilter(request, response);
	}
}
*/


/*@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
			throws jakarta.servlet.ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

	   String jwtToken = null;
	   String username = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
	   if (requestTokenHeader != null) {
		   logger.info("Authorization header: " + requestTokenHeader);

		   if (requestTokenHeader.startsWith("Bearer ")) {
			   jwtToken = requestTokenHeader.substring(7);
			   logger.info("Extracted JWT token: " + jwtToken);
		   } else {
			   logger.warn("JWT Token does not begin with Bearer String");
		   }
	   } else {
		   logger.warn("Authorization header is missing");
	   }

	   // Further JWT processing (e.g., extracting username, validating token) can be added here

	   filterChain.doFilter(request, response);
	   /*if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			 jwtToken = requestTokenHeader.substring(7);
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		filterChain.doFilter(request, response);*/




