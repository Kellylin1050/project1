package com.example.project1.Service.impl;

import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Service.JwtGeneratorService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource(value = {"classpath:application.properties"})
public class JwtGeneratorImpl implements JwtGeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(JwtGeneratorImpl.class);
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Value("${jwt.secret}")
    private String secret;

    //@Value("${jwt.refreshSecret}")
    //private String refreshSecret;

    @Value("${jwt.header}")
    private String message;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Key getRefreshSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Map<String, String> generateToken(String username) {
        String jwtToken = "";
        jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        String refreshToken = generateRefreshToken(username);
        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("refreshToken",refreshToken);
        jwtTokenGen.put("message", message);

        return jwtTokenGen;
    }
    @Override
    public String generateRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 )) // 7 day expiration
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                System.out.println("Token is blacklisted");
                return false;
            }
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            System.out.println("Token is valid");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token is invalid: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validateRefreshToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getRefreshSigningKey()).build().parseClaimsJws(token);
            return true;
        }catch (JwtException |IllegalArgumentException e){
            logger.error("Refresh token validation failed: " +e.getMessage());
            return false;
        }
    }



    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}