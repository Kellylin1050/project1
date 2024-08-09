package com.example.project1.Service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);
    private Set<String> blacklist = new HashSet<>();

    public void addTokenToBlacklist(String token) {
        logger.debug("Adding token to blacklist: " + token);
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {

        return blacklist.contains(token);
    }

}
