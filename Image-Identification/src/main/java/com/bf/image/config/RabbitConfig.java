package com.bf.image.config;

import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchTenRabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory, SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory) {
        // 设置连接工厂
        simpleRabbitListenerContainerFactory.setConnectionFactory(rabbitConnectionFactory);

        // 设置预取消息数量为 1
        simpleRabbitListenerContainerFactory.setPrefetchCount(1);

        // 设置接收后的消息处理器
        simpleRabbitListenerContainerFactory.setAfterReceivePostProcessors(new MessagePostProcessor[]{new AfterMessageReceivePostProcessor()});

        // 返回配置后的监听器工厂
        return simpleRabbitListenerContainerFactory;
    }

    public class AfterMessageReceivePostProcessor implements MessagePostProcessor {
        public AfterMessageReceivePostProcessor() {
        }

        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            Object traceId = headers.get("trace_id");
            MDC.put("trace_id", (String)traceId);
            String messageId = message.getMessageProperties().getMessageId();
            return messageId == null ? message : message;
        }
    }

}
