package com.dean.springamqp.mq;


/**
 * 上面的Listener类是实现了MessageListener接口的类，当容器接收到消息后，会自动触发onMessage方法。
 * 如果我们想使用普通的POJO类作为Message Listener，需要引入org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter类
 */
public class ConsumerDelegate {
    public void processMessage(Object message) {
        //这里接收的消息对象仅是消息体，不包含MessageProperties
        //如果想获取带MessageProperties的消息对象，需要在Adpater中
        //定义MessageConverter属性。
        String messageContent = message.toString();
        System.out.println(messageContent);
    }
}