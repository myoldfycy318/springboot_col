package com.configuration.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqBeans {

    @Bean
    public Queue Queue() {
        return new Queue("hello");
    }

    /**
     * 一个消息产生者，多个消息的消费者。竞争抢消息
     *
     * @return
     */
    @Bean
    public Queue Queue2() {
        return new Queue("neo");
    }


    @Bean
    public Queue aMessage() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue bMessage() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue cMessage() {
        return new Queue("fanout.C");
    }


    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    Binding bindingExchangeA(Queue aMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(aMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue bMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(bMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue cMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(cMessage).to(fanoutExchange);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }


    @Bean
    public Queue queueA() {
        return new Queue("topic.A");
    }

    @Bean
    public Queue queueB() {
        return new Queue("topic.B");
    }


    @Bean
    Binding bindingTopicExchange1(Queue queueA, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueA).to(topicExchange).with("topic.message");
    }


    @Bean
    Binding bindingTopicExchange2(Queue queueB, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueB).to(topicExchange).with("topic.#");
    }

    /**
     * 高并发情况下的消息产生和消费，这会产生一个消息丢失的问题。万一客户端在处理消息的时候挂了，那这条消息就相当于被浪费了，针对这种情况，rabbitmq推出了消息ack机制，熟悉tcp三次握手的一定不会陌生。
     * @return
     */
 /*   @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(Queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);//消息确认后才能删除
        container.setPrefetchCount(5);//每次处理5条消息
        container.setMessageListener(new ChannelAwareMessageListener() {

            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("消费端接收到消息 : " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        return container;
    }*/

}
