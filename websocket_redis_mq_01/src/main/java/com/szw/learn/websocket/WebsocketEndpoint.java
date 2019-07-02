package com.szw.learn.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.szw.learn.redismq.PublishService;
import com.szw.learn.redismq.SubscribeListener;
import com.szw.learn.util.SpringUtils;
/**
 *@ServerEndpoint(value="/websocket")value值必须以/开路
 *备注:@ServerEndpoint注解类不支持使用@Autowire
 *{topic}指：向哪个频道主题里发消息
 *{myname}指：这个消息是谁的。真实环境里可以使用当前登录用户信息
 */
@Component
@ServerEndpoint(value="/websocket/{topic}/{myname}")
public class WebsocketEndpoint {
	
	/**
	 * 因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
	 */
	private StringRedisTemplate redisTampate = SpringUtils.getBean(StringRedisTemplate.class);
	
	private RedisMessageListenerContainer redisMessageListenerContainer = SpringUtils.getBean(RedisMessageListenerContainer.class);
	
	//存放该服务器该ws的所有连接。用处：比如向所有连接该ws的用户发送通知消息。
	private static CopyOnWriteArraySet<WebsocketEndpoint> sessions = new CopyOnWriteArraySet<>();
	
	private Session session;
	
	@OnOpen
	public void onOpen(Session session,@PathParam("topic")String topic){
		System.out.println("java websocket:打开连接");
		this.session = session;
		sessions.add(this);
		SubscribeListener subscribeListener = new SubscribeListener();
		subscribeListener.setSession(session);
		subscribeListener.setStringRedisTemplate(redisTampate);
		//设置订阅topic
		redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic(topic));
	}
	
	@OnClose
	public void onClose(Session session){
		System.out.println("java websocket:关闭连接");
		sessions.remove(this);
	}
	
	@OnMessage
	public void onMessage(Session session,String message,@PathParam("topic")String topic,@PathParam("myname")String myname) throws IOException{
		message = myname+"："+message;
		System.out.println("java websocket 收到消息=="+message);
		PublishService publishService = SpringUtils.getBean(PublishService.class);
		publishService.publish(topic, message);
	}
	
	@OnError
	public void onError(Session session,Throwable error){
		System.out.println("java websocket 出现错误");
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
