package com.denachina.webflux.function;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.BiFunction;

/**
 * @author shanmin.zhang
 * @date 18/12/13
 */
@Component
public class CalculatorHandler {

    /**
     * add
     * @param request
     * @return
     */
    public Mono<ServerResponse> add(final ServerRequest request) {
        return calcute(request, (v1, v2) -> v1 + v2);
    }

    public Mono<ServerResponse> subtract(final ServerRequest request) {
        return calcute(request, (v1, v2) -> v1 - v2);
    }


    public Mono<ServerResponse> multiply(final ServerRequest request) {
        return calcute(request, (v1, v2) -> v1 * v2);
    }


    public Mono<ServerResponse> divide(final ServerRequest request) {
        return calcute(request, (v1, v2) -> v1 / v2);
    }

    private Mono<ServerResponse> calcute(final ServerRequest request, final BiFunction<Integer, Integer, Integer> biFunction) {
        Tuple2<Integer, Integer> tuple2 = extractOperands(request);
        return ServerResponse.ok().body(Mono.just(biFunction.apply(tuple2.getT1(), tuple2.getT2())), Integer.class);
    }


    private Tuple2<Integer, Integer> extractOperands(ServerRequest request) {
        return Tuples.of(parseOperand(request, "v1"), parseOperand(request, "v2"));
    }

    private int parseOperand(final ServerRequest request, final String param) {
        try {
            return Integer.parseInt(request.queryParam(param).orElse("0"));
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

}
