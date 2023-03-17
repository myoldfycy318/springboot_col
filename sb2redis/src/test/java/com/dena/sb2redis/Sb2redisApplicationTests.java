package com.dena.sb2redis;

import com.alibaba.fastjson.JSONObject;
import com.dena.sb2redis.pojo.Address;
import com.dena.sb2redis.pojo.UserDTO;
import com.denachina.common.service.redis.RedisRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Sb2redisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {}

    @Test
    public void userFastJSon() {

        UserDTO userDTO = new UserDTO();
        Address address = new Address();
        address.setCity("上海");
        address.setCountry("中国");
        userDTO.setAddress(address);
        userDTO.setId(1);
        userDTO.setFirstName("三");
        userDTO.setLastName("长");

        redisTemplate.opsForValue().set("user", userDTO, 10, TimeUnit.MINUTES);

        UserDTO userDTO1 = (UserDTO)redisTemplate.opsForValue().get("user");
        System.out.println("----------------->>>>>>" + JSONObject.toJSONString(userDTO1));

    }

    @Test
    public void increment() {
        String key = "increment";
        Long result = redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, 3, TimeUnit.SECONDS);
        System.out.println("1--->:" + result);
        result = redisTemplate.opsForValue().increment(key, 1);
        System.out.println("2--->:" + result);
        System.out.println("3--->:" + redisTemplate.opsForValue().get(key));
        redisTemplate.opsForValue().increment("day", 1L);

        List<Integer> list1 =
            redisTemplate.opsForValue().multiGet(Stream.of("day1", "year1").collect(Collectors.toList()));
        System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(list1));

        list1.stream().filter(item -> item != null).forEach(System.out::println);

    }

    @Autowired
    RedisRateLimiter rateLimiter;

    @Test
    public void rateLimitTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        String key = "test_rateLimit_key";
        int max = 4; // 令牌桶大小
        int rate = 4; // 令牌每秒恢复速度
        AtomicInteger successCount = new AtomicInteger(0);
        Executor executor = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(400);
        for (int i = 0; i < 400; i++) {
            executor.execute(() -> {
                boolean isAllow = rateLimiter.rateLimit(key, max, rate);
                if (isAllow) {
                    successCount.addAndGet(1);
                    log.info("T->{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
                log.info("F->{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                countDownLatch.countDown();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        countDownLatch.await();
        log.info("请求成功{}次", successCount.get());

        log.info("处理总时长{}/ms", (System.currentTimeMillis() - startTime));
    }

}
