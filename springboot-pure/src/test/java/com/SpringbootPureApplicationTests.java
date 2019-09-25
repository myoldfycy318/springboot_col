package com;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootPureApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @Test
    public void test() {
        redisTemplate.opsForValue().increment("key", 1);
        Object result = redisTemplate.opsForValue().get("key");
        redisTemplate.opsForHash().increment("hash", "a", 1);
        log.error("-->" + result);
        result = redisTemplate.opsForHash().get("hash", "a");
        log.info("2--->" + result);
    }


    @Test
    public void test1() throws InterruptedException {

        CountDownLatch downLatch = new CountDownLatch(500);

        for (int i = 0; i < 500; i++) {

            CompletableFuture.runAsync(() -> {
                redisTemplate.opsForValue().increment("key", 1);
                downLatch.countDown();
            });

        }
        downLatch.await();
        Object result = redisTemplate.opsForValue().get("key");
        log.info("2--->" + result);
    }


}
