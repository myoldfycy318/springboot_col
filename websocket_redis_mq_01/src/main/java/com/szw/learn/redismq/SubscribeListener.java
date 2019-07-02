package com.szw.learn.redismq;

import java.io.IOException;

import javax.websocket.Session;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 七脉 描述：订阅监听类
 */
public class SubscribeListener implements MessageListener {
	
    private StringRedisTemplate stringRedisTemplate;
	
    private Session session;
    
    /**
	 * 订阅接收发布者的消息
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String msg = new String(message.getBody());
		System.out.println(new String(pattern) + "主题发布：" + msg);
        if(null!=session){
        	try {
				session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
    
}
