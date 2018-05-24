package com.mq.simple;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleMqTests {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Test
    public void simple() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }


    public void send(int i) {
        String context = "spirng boot neo queue" + " ****** " + i;
        System.out.println("Sender1 : " + context);
        this.rabbitTemplate.convertAndSend("neo", context);
    }


    /**
     * 工作模式
     * 工作模式是同一个消息只能有一个消费者。
     * 一个消息产生者，多个消息的消费者。竞争抢消息
     */
    @Test
    public void race() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            send(i);
        }

        Thread.sleep(10*1000);
    }


    /**
     * 发布订阅模式
     * 生产者将消息不是直接发送到队列，而是发送到X交换机，然后由交换机发送给两个队列，两个消费者各自监听一个队列，来消费消息。
     * 这种方式实现同一个消息被多个消费者消费。工作模式是同一个消息只能有一个消费者。
     */
    @Test
    public void publish_subscriber(){
        String context = "hi, fanout msg ";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("fanoutExchange","", context);
    }

}
