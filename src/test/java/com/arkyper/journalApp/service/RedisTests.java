package com.arkyper.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    @Test
    void redisWorking() {
        redisTemplate.opsForValue().set("name", "Arkyper");
        Object name = redisTemplate.opsForValue().get("name");
        int a = 1;
    }
}
