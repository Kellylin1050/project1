package com.example.project1.Service.impl;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);
    private static final String BlackList_KEY ="blacklisted_tokens";
    //private Set<String> blacklist = new HashSet<>();

    @Autowired
    private RedisTemplate<String ,Object> redisTemplate;

    public void addTokenToBlacklist(String token, long expirationTimeInSeconds) {
        logger.debug("Adding token to blacklist: " + token);
        String key = "blacklist:" + token;
        redisTemplate.opsForValue().set(key, String.valueOf(true),expirationTimeInSeconds,TimeUnit.SECONDS);
        //redisTemplate.expire(BlackList_KEY, expirationTimeInSeconds, TimeUnit.SECONDS);
        //redisTemplate.opsForSet().add(BlackList_KEY , token);
        //blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        String key = "blacklist:" + token;
        String isBlacklisted = (String) redisTemplate.opsForValue().get(key);
        return "true".equals(isBlacklisted);
        //return redisTemplate.hasKey(token);
        //return redisTemplate.opsForSet().isMember(BlackList_KEY,token);
        //return blacklist.contains(token);
    }

}
