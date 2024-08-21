package com.example.project1.Service;

import com.example.project1.Dto.UserLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface JwtGeneratorService {
    Map<String, String> generateToken(String username);
    String generateRefreshToken(String username);

    String resolveToken(HttpServletRequest request);

    boolean validateToken(String token);
    boolean validateRefreshToken(String token);
    String getUsernameFromToken(String token);

    void storeTokenInRedis(String token, String type, long expirationTimeInSeconds);


}
