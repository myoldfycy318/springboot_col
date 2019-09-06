package com.china.gateway.filter.gobal;

import com.china.gateway.util.AesUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RequestEncryptionGlobalFilter
 *
 * @author shanmin.zhang
 * @date 2019-08-26
 **/

@Slf4j
@Component
public class RequestEncryptionGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private ObjectMapper objectMapper;

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return processRequest(exchange, chain);
    }

    private Mono<Void> processRequest(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerRequest serverRequest = new DefaultServerRequest(exchange, messageReaders);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();

        Mono<String> rawBody = serverRequest.bodyToMono(String.class).map(s -> s);

        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(rawBody, String.class);

        HttpHeaders tempHeaders = new HttpHeaders();
        tempHeaders.putAll(exchange.getRequest().getHeaders());
        tempHeaders.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, tempHeaders);

        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            Flux<DataBuffer> body = outputMessage.getBody();
            DataBufferHolder holder = new DataBufferHolder();
            body.subscribe(dataBuffer -> {
                int len = dataBuffer.readableByteCount();
                holder.length = len;
                byte[] bytes = new byte[len];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                String text = new String(bytes, StandardCharsets.UTF_8);
                JsonNode jsonNode = readNode(text);
                JsonNode payload = jsonNode.get("payload");
                String payloadText = payload.asText();
                byte[] content = AesUtils.X.decrypt(payloadText);
                String requestBody = new String(content, StandardCharsets.UTF_8);
                log.info("修改请求体payload,修改前:{},修改后:{}", payloadText, requestBody);
                rewritePayloadNode(requestBody, jsonNode);
                DataBuffer data = bufferFactory.allocateBuffer();
                data.write(jsonNode.toString().getBytes(StandardCharsets.UTF_8));
                holder.dataBuffer = data;
            });
            ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {

                @Override
                public HttpHeaders getHeaders() {
                    long contentLength = tempHeaders.getContentLength();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(super.getHeaders());
                    if (contentLength > 0) {
                        httpHeaders.setContentLength(contentLength);
                    } else {
                        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                    }
                    return httpHeaders;
                }

                @Override
                public Flux<DataBuffer> getBody() {
                    return Flux.just(holder.dataBuffer);
                }
            };
            return chain.filter(exchange.mutate().request(requestDecorator).build());
        }));
    }

    private void rewritePayloadNode(String text, JsonNode root) {
        try {
            JsonNode node = objectMapper.readTree(text);
            ObjectNode objectNode = (ObjectNode) root;
            objectNode.set("payload", node);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void setPayloadTextNode(String text, JsonNode root) {
        try {
            ObjectNode objectNode = (ObjectNode) root;
            objectNode.set("payload", new TextNode(text));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private JsonNode readNode(String in) {
        try {
            return objectMapper.readTree(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private class DataBufferHolder {

        DataBuffer dataBuffer;
        int length;
    }
}