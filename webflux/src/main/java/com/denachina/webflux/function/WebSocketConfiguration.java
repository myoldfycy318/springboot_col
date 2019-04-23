package com.denachina.webflux.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shanmin.zhang
 * @date 18/12/13
 */

@Configuration
public class WebSocketConfiguration {

    /**
     * websocket 客户端工具 https://www.websocket.org/echo.html
     *
     * @param localWebSocketHandler
     * @return
     */
    @Autowired
    @Bean
    public HandlerMapping webSocketMapping(LocalWebSocketHandler localWebSocketHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>(1);
        map.put("/chat", localWebSocketHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        mapping.setUrlMap(map);
        return mapping;
    }


    /**
     * 仅创建一个WebSocketHandler是不够的，我们还需要把它注册到 WebFlux 中。
     * 我们需要创建一个WebSocketHandlerAdapter对象，该对象负责把WebSocketHandler关联到WebFlux中。其中我们使用HandlerMapping把LocalWebSocketHandler映射到/chat端点。
     *
     * @return
     */
    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
