package com.china.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * ModifyReqBodyGatewayFilterFactory
 *
 * @author shanmin.zhang
 * @date 2019-08-26
 **/
@Slf4j
public class ModifyReqBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<ModifyReqBodyGatewayFilterFactory.Config> {

    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    private static final String KEY = "withParams";
    private final DataBufferFactory dataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY);
    }

    public ModifyReqBodyGatewayFilterFactory() {
        super(ModifyReqBodyGatewayFilterFactory.Config.class);
    }


    @Override
    public GatewayFilter apply(ModifyReqBodyGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
            ServerHttpRequest request = exchange.getRequest();
            String accessToken = request.getHeaders().getFirst("accessToken");
            if (!StringUtils.hasLength(accessToken)) {
                throw new IllegalArgumentException("accessToken");
            }
            // 新建一个ServerHttpRequest装饰器,覆盖需要装饰的方法
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {

                @Override
                public Flux<DataBuffer> getBody() {
                    Flux<DataBuffer> body = super.getBody();
                    InputStreamHolder holder = new InputStreamHolder();
                    body.subscribe(buffer -> holder.inputStream = buffer.asInputStream());
                    if (null != holder.inputStream) {
                        try {
                            // 解析JSON的节点
                            JsonNode jsonNode = objectMapper.readTree(holder.inputStream);
                            Assert.isTrue(jsonNode instanceof ObjectNode, "JSON格式异常");
                            ObjectNode objectNode = (ObjectNode) jsonNode;
                            // JSON节点最外层写入新的属性
                            objectNode.put("userId", accessToken);
                            DataBuffer dataBuffer = dataBufferFactory.allocateBuffer();
                            String json = objectNode.toString();
                            log.info("最终的JSON数据为:{}", json);
                            dataBuffer.write(json.getBytes(StandardCharsets.UTF_8));
                            return Flux.just(dataBuffer);
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    } else {
                        return super.getBody();
                    }
                }
            };

            // 使用修改后的ServerHttpRequestDecorator重新生成一个新的ServerWebExchange
            return chain.filter(exchange.mutate().request(decorator).build());
        };
    }

    private class InputStreamHolder {

        InputStream inputStream;
    }

    public static class Config {

        private boolean withParams;

        public boolean isWithParams() {
            return withParams;
        }

        public void setWithParams(boolean withParams) {
            this.withParams = withParams;
        }

    }
}
