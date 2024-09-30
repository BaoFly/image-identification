package com.bf.image.config.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;

@Data
@Slf4j
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedCorrelationData extends CorrelationData {

    // 消息内容
    private volatile Object message;

    // 交换机名称
    private String exchange;

    // 路由Key
    private String routingKey;

    // 重试次数
    private int retryCount;

    public ExtendedCorrelationData(String id) {
        super(id);
        this.retryCount = 0;
    }

    public ExtendedCorrelationData(String id, Object data) {
        this(id);
        this.message = data;
    }

}
