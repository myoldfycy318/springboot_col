package com.szw.learn.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketConfig {
	/**
     * <br>描 述:    @Endpoint注解的websocket交给ServerEndpointExporter自动注册管理
     * @return
     */
	@Bean
	public ServerEndpointExporter serverEndpointExporter(){
		return new ServerEndpointExporter();
	}
}
