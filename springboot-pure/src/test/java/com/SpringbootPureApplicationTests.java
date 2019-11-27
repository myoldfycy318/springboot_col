package com;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import sun.rmi.runtime.Log;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
        Long ret = redisTemplate.opsForHash().increment("hash", "a", 1);
        Long ret1 = redisTemplate.opsForHash().increment("hash", "c", 1);
        log.error("-->" + result);
        log.info("ret：" + ret);
        log.info("ret1：" + ret1);
        log.info(redisTemplate.opsForHash().get("hash", "a").toString());
        String mutexKey = "x";
        System.out.println(redisTemplate.opsForValue().setIfAbsent(mutexKey, "1"));
        redisTemplate.expire(mutexKey, 1, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().setIfAbsent(mutexKey, "1"));
//        redisTemplate.delete(mutexKey);
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


    @Test
    public void test2() throws InterruptedException {

        CountDownLatch downLatch = new CountDownLatch(2);
        String key = "lcm:paypal:tk";
        redisTemplate.delete(key);
        for (int i = 0; i < 2; i++) {

            CompletableFuture.runAsync(() -> {
                long start = System.currentTimeMillis();
                String aa = getPaypalAccessTokenTest(0);
                System.out.println("--->" + Thread.currentThread().getName() + "  ,-->:" + aa + ",time:" + (System.currentTimeMillis() - start) + "ms");
                downLatch.countDown();
            });

        }
        downLatch.await();
        getPaypalAccessTokenTest(0);
        Object result = redisTemplate.opsForValue().get(key);
        log.info("2--->" + result);
    }


    @Test
    public void time() {
        long start = System.currentTimeMillis();
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--->" + Thread.currentThread().getName() + ",time:" + (System.currentTimeMillis() - start) + "ms");
    }


    private String getPaypalAccessTokenTest(int count) {
        count++;
        String curThread = Thread.currentThread().getName();
        String key = "lcm:paypal:tk";
        String accessToken = redisTemplate.opsForValue().get(key);
        System.out.println(curThread + "--->count:" + count + "-》" + accessToken);
        String mutexKey = "lcm:paypal:tk:mk";
        if (!StringUtils.hasLength(accessToken)) {
            if (redisTemplate.opsForValue().setIfAbsent(mutexKey, "1")) {
                redisTemplate.expire(mutexKey, 500, TimeUnit.MILLISECONDS);
                accessToken = getPaypalAccessToken();
                redisTemplate.opsForValue().set(key, accessToken, 7, TimeUnit.HOURS);
                redisTemplate.delete(mutexKey);
            } else {
                try {
                    if (count > 3) {
                        accessToken = getPaypalAccessToken();
                        System.out.println(curThread + "--->count:" + count + ",token:" + accessToken);
                        return accessToken;
                    }
                    TimeUnit.MILLISECONDS.sleep(200);
                    return getPaypalAccessTokenTest(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        return accessToken;
    }


    String getPaypalAccessToken() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "x";
    }

}
