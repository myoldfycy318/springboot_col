package com.denachina.webflux.function;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;


/**
 * @author shanmin.zhang
 * @date 18/12/13
 * <p>
 * WebFlux 也对创建 WebSocket 服务器端提供了支持。
 * 在服务器端，我们需要实现接口org.springframework.web.reactive.socket.WebSocketHandler来处理 WebSocket 通讯，
 * 其handle方法的参数是WebSocketSession对象，可以用来获取客户端信息、接送消息和发送消息。
 */
@Component
public class LocalWebSocketHandler implements WebSocketHandler {

    private Logger logger = LoggerFactory.getLogger(LocalWebSocketHandler.class);

    @Override
    public Mono<Void> handle(WebSocketSession socketSession) {
        logger.info(JSONObject.toJSONString(socketSession));
        return socketSession.send(
                socketSession.receive().log("->->")
                        .map(socketMessage -> socketSession.textMessage("回复-》".concat(socketMessage.getPayloadAsText())))
        );
    }
}
