package com.denachina.webflux;import com.denachina.webflux.pojo.MsgEvent;import com.denachina.webflux.pojo.User;import org.junit.Test;import org.springframework.core.ParameterizedTypeReference;import org.springframework.http.MediaType;import org.springframework.http.codec.ServerSentEvent;import org.springframework.web.reactive.function.BodyExtractors;import org.springframework.web.reactive.function.client.WebClient;import org.springframework.web.reactive.socket.WebSocketMessage;import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;import org.springframework.web.reactive.socket.client.WebSocketClient;import reactor.core.publisher.Flux;import reactor.core.publisher.Mono;import java.net.URI;import java.time.Duration;import java.util.Date;import java.util.Objects;import java.util.concurrent.CountDownLatch;/** * WebClientTest * * @author shanmin.zhang * @date 18/12/6 **/public class WebClientTest {    private String url = "http://localhost:9091";    @Test    public void webClientTest1() {        WebClient webClient = WebClient.create(url);        webClient.get().uri("/health/check")                .retrieve()                .bodyToMono(String.class)                .log()                .block();    }    /**     * 在客户端，WebClient可以接收text/event-stream和application/stream+json格式的数据流，     * 也可以在请求的时候上传一个数据流到服务器；     */    @Test    public void testFlux() {        WebClient webClient = WebClient.create(url);        webClient.get().uri("/user")                .accept(MediaType.APPLICATION_STREAM_JSON)                .exchange()                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(User.class))                //只读地peek每个元素，然后打印出来，它并不是subscribe，所以不会触发流                .log()                //在收到最后一个元素前会阻塞，响应式业务场景中慎用。                .blockLast();    }    @Test    public void saveTest() {        CountDownLatch latch = new CountDownLatch(1);        User user = new User();        user.setBirthday(new Date());        user.setEmail("12@qq.com");        user.setName("san");        user.setPhone("110");        user.setUsername("zhangsan");        WebClient webClient = WebClient.create(url);        webClient.post().uri("/user")                .accept(MediaType.APPLICATION_JSON)                .body(Mono.just(user), User.class)                .exchange()                .flatMap(clientResponse -> clientResponse.bodyToMono(User.class))                .subscribe(System.out::println, System.err::println, latch::countDown);        try {            latch.await();        } catch (InterruptedException e) {            e.printStackTrace();        }    }    /**     * SERVER SEND EVENT     * SSE     */    @Test    public void test服务端推送() {        WebClient webClient = WebClient.create(url);        webClient.get().uri("times")                .accept(MediaType.TEXT_EVENT_STREAM)                .retrieve()                .bodyToFlux(String.class)                .log()                //由于/times是一个无限流，这里取前10个，会导致流被取消；                .take(10)                .blockLast();    }    /**     * 在客户端，WebClient可以接收text/event-stream和application/stream+json格式的数据流，     * 也可以在请求的时候上传一个数据流到服务器；     * <p>     * 声明速度为每秒一个MyEvent元素的数据流，不加take的话表示无限个元素的数据流；     * 声明请求体的数据格式为application/stream+json；     * body方法设置请求体的数据。     */    @Test    public void test模拟客户端间歇发送数据() {        Flux<MsgEvent> msgEventFlux = Flux.interval(Duration.ofSeconds(2L))                .map(i -> new MsgEvent.Builder().username("zhangsan").message("msg" + i).build())                .take(10);        WebClient webClient = WebClient.create(url);        webClient.post().uri("/msgEvent")                .contentType(MediaType.APPLICATION_STREAM_JSON)                .body(msgEventFlux, MsgEvent.class)                .retrieve()                .bodyToMono(Void.class)                .block();    }    /**     * 由于 SSE 服务的响应是一个消息流，我们需要使用flatMapMany把 Mono<ServerResponse>转换成一个Flux<ServerSentEvent>对象，     * 这是通过方法BodyExtractors.toFlux来完成的，其中的参数new ParameterizedTypeReference<ServerSentEvent<String>>() {}表明了响应消息流中的内容是ServerSentEvent对象     * <p>     * 由于 SSE 服务器会不断地发送消息，这里我们只是通过buffer方法来获取前 10 条消息并输出     * <p>     * 只读地 peek 每个元素，然后打印出来，它并不是 subscribe，所以不会触发流     * <p>     * blockFirst方法，顾名思义，在收到第一个元素前会阻塞，响应式业务场景中慎用     */    @Test    public void testSSE() {        WebClient webClient = WebClient.create();        webClient.get().uri("http://localhost:9091/random")                .accept(MediaType.TEXT_EVENT_STREAM)                .exchange()                .flatMapMany(clientResponse -> clientResponse.body(                        BodyExtractors.toFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {                        })                ))                .filter(sse -> Objects.nonNull(sse.data()))                .map(sse -> sse.data())                .buffer(10)                .doOnNext(System.out::println)                .blockFirst();    }    @Test    public void testSEEClient2() {        WebClient client = WebClient.create();        client.get()                .uri("http://localhost:9091/random")                .accept(MediaType.TEXT_EVENT_STREAM)                .retrieve()                .bodyToFlux(String.class)                .log()                .take(10)                .blockLast();    }    /**     * 创建一个 WebSocketClient 实例     * <p>     * 使用 WebSocketClient 的execute方法与 WebSocket 服务器建立连接，并执行给定的 WebSocketHandler 对象     * <p>     * 通过 WebSocketSession 的send方法来发送字符串”Hello” 到服务器端     * <p>     * 通过receive方法来等待服务器端的响应并输出，take(1)的作用是表明客户端只获取服务器端发送的第一条消息     */    @Test    public void testWebSocketClient() {        WebSocketClient client = new ReactorNettyWebSocketClient();        client.execute(URI.create("ws://localhost:9091/chat"), webSocketSession                -> webSocketSession.send(Flux.just(webSocketSession.textMessage("hello")))                .thenMany(webSocketSession.receive().take(1).map(WebSocketMessage::getPayloadAsText))                .doOnNext(System.out::println)                .then()        ).block(Duration.ofSeconds(5));    }}