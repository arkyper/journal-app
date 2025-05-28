package com.arkyper.journalApp.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj == null) {
                return null;
            }
            return objectMapper.readValue(obj.toString(), typeReference);
        } catch(Exception e) {
            log.error("Exception ", e);
            return null;
        }
    }

    public void set(String key, Object obj, Long ttl) {
        try {
            String jsonString = objectMapper.writeValueAsString(obj);
            redisTemplate.opsForValue().set(key, jsonString, ttl, TimeUnit.MINUTES);
        } catch(Exception e) {
            log.error("Exception ", e);
        }
    }

}
