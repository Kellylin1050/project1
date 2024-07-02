package com.example.project1.Service;

import com.example.project1.Entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface JwtGeneratorService {
    Map<String, String> generateToken(User user);
}
