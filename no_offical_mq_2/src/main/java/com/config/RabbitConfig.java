package com.config;

import com.constants.AppConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfig {

    //org.springframework.boot.autoconfigure.amqp.RabbitProperties类，记录application.properties中的RabbitMQ连接设置。
    @Autowired
    private RabbitProperties rabbitProperties;

    @Bean("connectionFactory")
    public ConnectionFactory getConnectionFactory() {
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory =
                new com.rabbitmq.client.ConnectionFactory();
        rabbitConnectionFactory.setHost(rabbitProperties.getHost());
        rabbitConnectionFactory.setPort(rabbitProperties.getPort());
        rabbitConnectionFactory.setUsername(rabbitProperties.getUsername());
        rabbitConnectionFactory.setPassword(rabbitProperties.getPassword());
        rabbitConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());

        ConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
        return connectionFactory;
    }

    /**
     * 使用RabbitAdmin对象定义发送消息Exchange/Queue/Binding,
     *
     * @return
     */
    @Bean(name = "rabbitAdmin")
    public RabbitAdmin getRabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(getConnectionFactory());
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }


    @Bean(name = "serializerMessageConverter")
    public MessageConverter getMessageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean(name = "messagePropertiesConverter")
    public MessagePropertiesConverter getMessagePropertiesConverter() {
        return new DefaultMessagePropertiesConverter();
    }

    @Bean(name = "rabbitTemplate")
    public RabbitTemplate getRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(getConnectionFactory());
        rabbitTemplate.setUseTemporaryReplyQueues(false);
        rabbitTemplate.setMessageConverter(getMessageConverter());
        rabbitTemplate.setMessagePropertiesConverter(getMessagePropertiesConverter());
        rabbitTemplate.setReplyAddress(AppConstants.REPLY_QUEUE_NAME);
        rabbitTemplate.setReceiveTimeout(60000);
        return rabbitTemplate;
    }


    @Bean(name = "springMessageQueue")
    public Queue createQueue(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
        Queue sendQueue = new Queue(AppConstants.SEND_QUEUE_NAME, true, false, false);
        rabbitAdmin.declareQueue(sendQueue);
        return sendQueue;
    }

    @Bean(name = "springMessageExchange")
    public Exchange createExchange(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
        DirectExchange sendExchange = new DirectExchange(AppConstants.SEND_EXCHANGE_NAME, true, false);
        rabbitAdmin.declareExchange(sendExchange);
        return sendExchange;
    }

    @Bean(name = "springMessageBinding")
    public Binding createMessageBinding(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
        Map<String, Object> arguments = new HashMap<String, Object>();
        Binding sendMessageBinding =
                new Binding(AppConstants.SEND_QUEUE_NAME, Binding.DestinationType.QUEUE,
                        AppConstants.SEND_EXCHANGE_NAME, AppConstants.SEND_MESSAGE_KEY, arguments);
        rabbitAdmin.declareBinding(sendMessageBinding);
        return sendMessageBinding;
    }

    @Bean(name = "springReplyMessageQueue")
    public Queue createReplyQueue(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
        Queue replyQueue = new Queue(AppConstants.REPLY_QUEUE_NAME, true, false, false);
        rabbitAdmin.declareQueue(replyQueue);
        return replyQueue;
    }

    @Bean(name = "springReplyMessageExchange")
    public Exchange createReplyExchange(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
        DirectExchange replyExchange = new DirectExchange(AppConstants.REPLY_EXCHANGE_NAME, true, false);
        rabbitAdmin.declareExchange(replyExchange);
        return replyExchange;
    }

    @Bean(name = "springReplyMessageBinding")
    public Binding createReplyMessageBinding(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
        Map<String, Object> arguments = new HashMap<String, Object>();
        Binding replyMessageBinding =
                new Binding(AppConstants.REPLY_QUEUE_NAME, Binding.DestinationType.QUEUE,
                        AppConstants.REPLY_EXCHANGE_NAME, AppConstants.REPLY_MESSAGE_KEY, arguments);
        rabbitAdmin.declareBinding(replyMessageBinding);
        return replyMessageBinding;
    }


    /**
     * 最后定义接收返回消息的Message Listener Container,
     * 这里的Listener属性设置的是上面创建的RabbitTemplate对象
     *
     * @return
     */
    @Bean(name = "replyMessageListenerContainer")
    public SimpleMessageListenerContainer createReplyListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(getConnectionFactory());
        listenerContainer.setQueueNames(AppConstants.REPLY_QUEUE_NAME);
        listenerContainer.setMessageConverter(getMessageConverter());
        listenerContainer.setMessageListener(getRabbitTemplate());
        listenerContainer.setRabbitAdmin(getRabbitAdmin());
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    /**
     * 定义了一个ThreadPoolExecutor对象，用于RabbitTemplate异步执行，发送和接收消息使用。
     * @return
     */
    @Bean(name = "threadExecutor")
    public ThreadPoolTaskExecutor createThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor =
                new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(200);
        threadPoolTaskExecutor.setKeepAliveSeconds(20000);
        return threadPoolTaskExecutor;
    }


}
