package io.example;

import io.vertx.core.Vertx;

/**
 * Main
 *
 * @author shanmin.zhang
 * @description:
 * @date 2020/9/30
 */

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MyFirstVerticle.class.getName());
    }
}
