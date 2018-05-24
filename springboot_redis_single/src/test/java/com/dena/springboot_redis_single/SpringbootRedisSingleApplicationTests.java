package com.dena.springboot_redis_single;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisSingleApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis() {
        Map<String, Object> map = new HashMap<>();
        map.put("dStoreId", "111");
        map.put("wxBind", "xx1");
        map.put("dAccessToken", "xxx22e3e");
        redisTemplate.opsForValue().set("k1", map);
        Map<String, Object> reslut = (Map<String, Object>) redisTemplate.opsForValue().get("k1");

        System.out.println(reslut);


    }

    @Test
    public void getRedis2() {
        for (int i = 0; i < 10; i++) {
            System.out.println(redisTemplate.opsForValue().get("k" + i));
        }
    }

    @Test
    public void redis事物操作() {
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.delete("test");
                operations.opsForValue().set("test", "2");
                Object val = operations.exec();
                return val;
            }
        };
        redisTemplate.execute(sessionCallback);
    }


    @Test
    public void cas() throws InterruptedException, ExecutionException {
        String key = "test-cas-1";
        ValueOperations<String, String> strOps = redisTemplate.opsForValue();
        strOps.set(key, "hello");
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Callable<Object>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int idx = i;
            tasks.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    return redisTemplate.execute(new SessionCallback() {
                        @Override
                        public Object execute(RedisOperations operations) throws DataAccessException {
                            operations.watch(key);
                            String origin = (String) operations.opsForValue().get(key);
                            operations.multi();
                            operations.opsForValue().set(key, origin + idx);
                            Object rs = operations.exec();
                            System.out.println("set:" + origin + idx + " rs:" + rs);
                            return rs;
                        }
                    });
                }
            });
        }
        List<Future<Object>> futures = pool.invokeAll(tasks);
        for (Future<Object> f : futures) {
            System.out.println(f.get());
        }
        pool.shutdown();
        pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void setNt() {
        String key = "setNt";
        boolean result = redisTemplate.opsForValue().setIfAbsent(key, 1);
        redisTemplate.expire(key, 300, TimeUnit.SECONDS);
        System.out.println("1--->:" + result);
        result = redisTemplate.opsForValue().setIfAbsent(key, 1);
        System.out.println("2--->:" + result);

    }


}
