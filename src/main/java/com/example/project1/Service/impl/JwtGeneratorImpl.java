package com.example.project1.Service.impl;

import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Service.JwtGeneratorService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource(value = {"classpath:application.properties"})
public class JwtGeneratorImpl implements JwtGeneratorService {


/*   public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claim()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 + 24))
                .and()
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    */


    @Autowired
    private TokenBlacklistService tokenBlacklistService;



    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.header}")
    private String message;


    @Override
    public Map<String, String> generateToken(UserLoginRequest userLoginRequest) {
        String jwtToken = "";
        jwtToken = Jwts.builder().setSubject(userLoginRequest.getEmail()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, secret).compact();
        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;
    }public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(25);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return false;
        }
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
            return true;
        }
        catch (Exception e) {
          return false;
        }
    }
}