package com.mq.simple;


import com.configuration.mq.RabbitMqAckBeans;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Mq_ACK_Tests {


    @Autowired
    private RabbitTemplate confirmCBRabbitTemplate;

    @Test
    public void send3() throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(uuid);
        confirmCBRabbitTemplate.convertAndSend(RabbitMqAckBeans.EXCHANGE, RabbitMqAckBeans.ROUTINGKEY2+"x", "哈哈", correlationId);

        Thread.sleep(1000);
    }
}
