package io.example;

import io.vertx.core.AbstractVerticle;

/**
 * MainVerticle
 *
 * @author shanmin.zhang
 * @description:
 * @date 2020/9/30
 */

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.deployVerticle(MyFirstVerticle.class.getName());
    }
}