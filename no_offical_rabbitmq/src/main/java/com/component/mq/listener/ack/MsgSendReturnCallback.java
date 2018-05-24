package com.component.mq.listener.ack;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MsgSendReturnCallback implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("发布返回-"+message+"--i= "+i+" ,s= "+s+", s1="+s1+", s2="+s2);
    }
}
