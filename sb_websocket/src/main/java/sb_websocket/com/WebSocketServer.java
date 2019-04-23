package sb_websocket.com;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
/**
 * Created by shanmin.zhang
 * Date: 18/8/14
 * Time: 下午4:43
 */

/**
 * 因为WebSocket是类似客户端服务端的形式(采用ws协议)，那么这里的WebSocketServer其实就相当于一个ws协议的Controller
 * 直接@ServerEndpoint("/websocket")启用即可，然后在里面实现@OnOpen,@onClose,@onMessage等方法
 */

@ServerEndpoint("/connect/{userId}")
@Component
public class WebSocketServer {

    private static Logger log = Logger.getLogger(WebSocketServer.class);

    /**
     * New Connected
     *
     * @param userId
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        log.info("[WebSocketServer] Connected : userId = " + userId);
        WebSocketUtils.add(userId, session);
    }

    /**
     * Send Message
     *
     * @param userId
     * @param message
     * @return
     */
    @OnMessage
    public String onMessage(@PathParam("userId") String userId,
                            String message) {
        log.info("[WebSocketServer] Received Message : userId = " + userId + " , message = " + message);
        if (message.equals("&")) {
            return "&";
        } else {
            WebSocketUtils.receive(userId, message);
            return "Got your message (" + message + ").";
        }
    }

    /**
     * Error
     *
     * @param userId
     * @param throwable
     * @param session
     */
    @OnError
    public void onError(@PathParam("userId") String userId,
                        Throwable throwable,
                        Session session) {
        log.info("[WebSocketServer] Connection Exception : userId = " + userId + " , throwable = " + throwable.getMessage());
        WebSocketUtils.remove(userId);
    }


    /**
     * Close Connection
     *
     * @param userId
     * @param session
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId,
                        Session session) {
        log.info("[WebSocketServer] Close Connection : userId = " + userId);
        WebSocketUtils.remove(userId);
    }
}