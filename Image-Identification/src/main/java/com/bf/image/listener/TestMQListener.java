package com.bf.image.listener;

import com.alibaba.fastjson.JSONObject;
import com.bf.image.constant.RabbitConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
@Slf4j
public class TestMQListener {

    @RabbitListener(
            containerFactory = "prefetchTenRabbitListenerContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(name = RabbitConstant.TEST_DIRECT_MQ),
                    exchange = @Exchange(
                            name = RabbitConstant.TEST_DIRECT_MQ,
                            type = ExchangeTypes.DIRECT
                    ),
                    key = {RabbitConstant.TEST_DIRECT_MQ}
            )
    )
    public void testDirectMQ(@Payload String body, Channel channel, Message message) throws IOException {
        log.info("处理MQ：【{}】，body：【{}】，message：【{}】，deliveryTag：【{}】", RabbitConstant.TEST_DIRECT_MQ, body, JSONObject.toJSONString(message), message.getMessageProperties().getDeliveryTag());
        System.out.println(body);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(
            containerFactory = "prefetchTenRabbitListenerContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(name = RabbitConstant.TEST_DELAY_MQ),
                    exchange = @Exchange(
                            name = RabbitConstant.TEST_DELAY_MQ,
                            arguments = @Argument(name = "x-delayed-type", value = "direct"),
                            delayed = Exchange.TRUE
                    ),
                    key = {RabbitConstant.TEST_DELAY_MQ}
            )
    )
    public void testDelayMQ(@Payload String body, Channel channel, Message message) throws IOException {
        log.info("处理MQ：【{}】，body：【{}】，message：【{}】，deliveryTag：【{}】", RabbitConstant.TEST_DELAY_MQ, body, JSONObject.toJSONString(message), message.getMessageProperties().getDeliveryTag());
        System.out.println(body);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
