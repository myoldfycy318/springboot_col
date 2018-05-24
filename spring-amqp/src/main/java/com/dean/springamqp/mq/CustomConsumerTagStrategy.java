package com.dean.springamqp.mq;

import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.stereotype.Component;

/**
 * 如果我们想设置消费者Tag为指定Tag，我们可以在Message Listener Container中 设置自定义consumer tag strategy。
 * 首先我们需要定义一个Consumer Tag Strategy类，它实现了ConsumerTagStrategy接口
 */
@Component
public class CustomConsumerTagStrategy implements ConsumerTagStrategy {
    @Override
    public String createConsumerTag(String queue) {
        String consumerName = "Consumer";
        return consumerName + "_" + queue;
    }
}