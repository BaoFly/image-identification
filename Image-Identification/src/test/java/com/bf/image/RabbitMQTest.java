package com.bf.image;

import com.bf.image.pojo.MessageWarning;
import com.bf.image.utils.MqUtils.RabbitSender;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.bf.image.constant.RabbitConstant.TEST_DELAY_MQ;
import static com.bf.image.constant.RabbitConstant.TEST_DIRECT_MQ;

@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private RabbitSender rabbitSender;



    @Test
    void testRedis1() {
        String content = "Hello Direct MQ, I will do that";

        for (int i = 0; i < 3; i++) {
            rabbitSender.sendMessage(
                    TEST_DIRECT_MQ,
                    TEST_DIRECT_MQ,
                    content + "" + i
            );
        }
    }

    @Test
    void testRedis2() {
        String content = "Hello Delay MQ, I will do that";

        for (int i = 0; i < 3; i++) {
            rabbitSender.sendMessage(
                    TEST_DELAY_MQ,
                    TEST_DELAY_MQ,
                    content + "" + i,
                    60
            );
        }
    }


}
