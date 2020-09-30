package io.example;

import io.vertx.core.AbstractVerticle;

/**
 * MyFirstVerticle
 *
 * @author shanmin.zhang
 * @description:
 * @date 2020/9/30
 */

public class MyFirstVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();

        vertx.createHttpServer().requestHandler(httpServerRequest -> {
            httpServerRequest.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello World");
        }).listen(8080);
    }
}
