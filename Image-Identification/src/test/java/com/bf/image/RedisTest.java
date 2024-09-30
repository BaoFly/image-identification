package com.bf.image;

import com.bf.image.pojo.MessageWarning;
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
    void testRedis2() {
        MessageWarning messageWarning = new MessageWarning()
                .setMessageMemo("I Will back")
                .setMessageName("Lark Warn")
                .setMessageUrl("http")
                .setCreateTime(new Date());
        redisTemplate.opsForValue().set("java:str:1", messageWarning);
    }

    @Test
    void testRedis3() {
        stringRedisTemplate.opsForHash().put("java:hash:1", "name", "borja");
    }

}
