package com.service;


import com.common.ApplicationContextUtil;
import com.constants.AppConstants;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;
import java.util.function.Supplier;

public class MQSenderSupplier implements Supplier<String> {

    private String message;

    public MQSenderSupplier(String message) {
        this.message = message;
    }

    public String get() {
        Date sendTime = new Date();
        String correlationId = UUID.randomUUID().toString();

        MessagePropertiesConverter messagePropertiesConverter =
                (MessagePropertiesConverter) ApplicationContextUtil.getBean("messagePropertiesConverter");

        RabbitTemplate rabbitTemplate =
                (RabbitTemplate) ApplicationContextUtil.getBean("rabbitTemplate");

        AMQP.BasicProperties props =
                new AMQP.BasicProperties("text/plain",
                        "UTF-8",
                        null,
                        2,
                        0, correlationId, AppConstants.REPLY_EXCHANGE_NAME, null,
                        null, sendTime, null, null,
                        "SpringProducer", null);

        MessageProperties sendMessageProperties =
                messagePropertiesConverter.toMessageProperties(props, null, "UTF-8");
        sendMessageProperties.setReceivedExchange(AppConstants.REPLY_EXCHANGE_NAME);
        sendMessageProperties.setReceivedRoutingKey(AppConstants.REPLY_MESSAGE_KEY);
        sendMessageProperties.setRedelivered(true);

        Message sendMessage = MessageBuilder.withBody(message.getBytes())
                .andProperties(sendMessageProperties)
                .build();

        Message replyMessage =
                rabbitTemplate.sendAndReceive(AppConstants.SEND_EXCHANGE_NAME,
                        AppConstants.SEND_MESSAGE_KEY, sendMessage);

        String replyMessageContent = null;
        try {
            replyMessageContent = new String(replyMessage.getBody(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return replyMessageContent;
    }
}

