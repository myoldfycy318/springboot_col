package com.dean.springamqp.mq;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

public class RabbitMQConsumer implements MessageListener {
    @Autowired
    private MessagePropertiesConverter messagePropertiesConverter;

    @Override
    public void onMessage(Message message) {
        try {
            //spring-amqp Message对象中的Message Properties属性
            MessageProperties messageProperties = message.getMessageProperties();
            //使用Message Converter将spring-amqp Message对象中的Message Properties属性
            //转换为RabbitMQ 的Message Properties对象
            AMQP.BasicProperties rabbitMQProperties =
                    messagePropertiesConverter.fromMessageProperties(messageProperties, "UTF-8");
            System.out.println("The message's correlationId is:" + rabbitMQProperties.getCorrelationId());
            String messageContent = null;
            messageContent = new String(message.getBody(), "UTF-8");
            System.out.println("The message content is:" + messageContent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}