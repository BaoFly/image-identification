package com.bf.image.utils.MqUtils;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bf.image.config.entity.ExtendedCorrelationData;
import com.bf.image.exception.CustomException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.ReturnCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Objects;


@Component
@Slf4j
public class RabbitSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback, InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private Integer retryCount = 3;


    private ExtendedCorrelationData initExtendedCorrelationData(Object message) {
        return new ExtendedCorrelationData(UUID.randomUUID().toString(), message);
    }

    private ExtendedCorrelationData getCorrelationData(String exchangeName, String routingKey, Object message) {
        if (StringUtils.isBlank(exchangeName) || StringUtils.isBlank(routingKey)) {
            throw new CustomException("交换机名称或路由Key不能为空");
        }

        if (Objects.isNull(message)) {
            throw new CustomException("消息内容不能为空");
        }

        ExtendedCorrelationData correlationData = this.initExtendedCorrelationData(message);

        correlationData.setExchange(exchangeName)
                .setRoutingKey(routingKey)
                .setMessage(message);

        String json = JSONObject.toJSONString(message);

        if (json.length() > 10240) {
            json = json.substring(0, 10240) + "...";
        }

        log.info("发送MQ消息，消息ID：{}，exchangeName:{}，routingKey:{}，消息体:{},",
                correlationData.getId(),
                exchangeName,
                routingKey,
                json);

        return correlationData;
    }


    public void sendMessage(String exchangeName, String routingKey, Object message, int delayTime) {
        ExtendedCorrelationData correlationData = this.getCorrelationData(exchangeName, routingKey, message);
        this.convertAndSend(exchangeName, routingKey, message, correlationData, delayTime * 1000);
    }

    public void sendMessage(String exchangeName, String routingKey, Object message) {
        ExtendedCorrelationData correlationData = this.getCorrelationData(exchangeName, routingKey, message);
        this.convertAndSend(exchangeName, routingKey, message, correlationData);
    }


    private void convertAndSend(String exchange,
                                String routingKey,
                                Object message,
                                ExtendedCorrelationData correlationData) throws AmqpException {
        String id = correlationData.getId();

        String[] traceId = new String[]{MDC.get("trade_id")};
        if (traceId[0] == null) {
            traceId[0] = MDC.get("trace_id");
        }

        try {
            this.rabbitTemplate.convertAndSend(exchange, routingKey, message, (msg) -> {
                MessageProperties messageProperties = msg.getMessageProperties();

                messageProperties.setMessageId(id);
                messageProperties.setHeader("trace_id", traceId[0]);
                messageProperties.setDelay(0);

                return msg;
            }, correlationData);
        } catch (Exception e) {
            log.error("MQ消息发送异常，消息ID：{}，exchangeName：{}，routingKey：{}，消息体：{}，异常堆栈：{}",
                    id,
                    exchange,
                    routingKey,
                    JSON.toJSONString(message),
                    e);
        }
    }

    private void convertAndSend(String exchange,
                                String routingKey,
                                Object message,
                                ExtendedCorrelationData correlationData,
                                int delayTime) throws AmqpException {
        String id = correlationData.getId();

        String[] traceId = new String[]{MDC.get("trade_id")};
        if (traceId[0] == null) {
            traceId[0] = MDC.get("trace_id");
        }

        try {
            this.rabbitTemplate.convertAndSend(exchange, routingKey, message, (msg) -> {
                MessageProperties messageProperties = msg.getMessageProperties();

                messageProperties.setMessageId(id);
                messageProperties.setHeader("trace_id", traceId[0]);
                messageProperties.setDelay(delayTime);

                return msg;
            }, correlationData);
        } catch (AmqpException e) {
            log.error("MQ消息发送异常，消息ID：{}，exchangeName：{}，routingKey：{}，消息体：{}，异常堆栈：{}",
                    id,
                    exchange,
                    routingKey,
                    JSON.toJSONString(message),
                    e);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            ExtendedCorrelationData correlationDataExtends = (ExtendedCorrelationData) correlationData;

            if (correlationDataExtends.getRetryCount() < this.retryCount) {
                log.info("MQ消息发送失败，消息重发，消息ID：{}，重发次数：{}，消息体：{}",
                        correlationDataExtends.getId(),
                        correlationDataExtends.getRetryCount(),
                        JSON.toJSONString(correlationDataExtends.getMessage()));

                correlationDataExtends.setRetryCount(correlationDataExtends.getRetryCount() + 1);

                this.convertAndSend(
                        correlationDataExtends.getExchange(),
                        correlationDataExtends.getRoutingKey(),
                        correlationDataExtends.getMessage(),
                        correlationDataExtends);

            } else {
                log.warn("MQ消息重发失败，消息入库，消息ID：{}，消息体:{}",
                        correlationData.getId(),
                        JSON.toJSONString(correlationDataExtends.getMessage()));
            }
        } else {
            log.info("消息发送成功,消息ID:{}", correlationData.getId());
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        int replyCode = returned.getReplyCode();
        if (replyCode == 312 || replyCode == 404 || replyCode == 405 || replyCode == 406) {
            log.error("MQ消息发送失败，replyCode：{}, replyText：{}，exchange：{}，routingKey：{}，消息体：{}",
                    replyCode,
                    returned.getReplyText(),
                    returned.getExchange(),
                    returned.getRoutingKey(),
                    JSON.toJSONString(returned.getMessage().getBody()));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            this.rabbitTemplate.setConfirmCallback(this);
            this.rabbitTemplate.setReturnsCallback(this);
        } catch (Exception var2) {
            log.error("RabbitTemplate设置confirmCallback&returnCallback,这两个回调可能已经设置过，失败原因：【{}】", var2.getMessage());
        }
    }
}
