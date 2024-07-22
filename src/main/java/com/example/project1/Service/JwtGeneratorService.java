package com.example.project1.Service;

import com.example.project1.Dto.UserLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface JwtGeneratorService {
    Map<String, String> generateToken(UserLoginRequest userLoginRequest);

    String resolveToken(HttpServletRequest request);

    boolean validateToken(String token);
}
