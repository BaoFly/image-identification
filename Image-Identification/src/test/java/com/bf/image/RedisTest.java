package com.bf.image;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedis1() {
        redisTemplate.opsForValue().set("java:str", "test");
    }


    @Test
    void testRedis3() {
        stringRedisTemplate.opsForHash().put("java:hash:1", "name", "borja");
    }

}
