package com.example.project1.Service.impl;


import com.example.project1.Dao.UserDao;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource(value = {"classpath:application.properties"})
public class JwtGeneratorImpl implements JwtGeneratorService {

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.secret}")
    private String message;


    @Override
    public Map<String, String> generateToken(User user) {
        String jwtToken="";
        jwtToken = Jwts.builder().setSubject(user.getUsername()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secret").compact();
        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;
    }
}