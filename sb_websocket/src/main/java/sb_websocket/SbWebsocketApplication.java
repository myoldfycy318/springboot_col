package sb_websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class SbWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbWebsocketApplication.class, args);
    }

    /**
     * 开启WebSocket支持
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
