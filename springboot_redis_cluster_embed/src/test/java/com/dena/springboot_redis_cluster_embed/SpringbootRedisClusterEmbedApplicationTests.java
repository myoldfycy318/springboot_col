package com.dena.springboot_redis_cluster_embed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisClusterEmbedApplicationTests {


    Logger logger = LoggerFactory.getLogger(SpringbootRedisClusterEmbedApplicationTests.class);

    @Test
    public void contextLoads() {
    }


    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void redisTest() {

        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

        //数据插入测试：
        for (int i = 0; i < 10; i++) {
            opsForValue.set("key" + i, "value" + i);
        }

        for (int i = 0; i < 10; i++) {

            String valueFromRedis = opsForValue.get("key" + i);
            logger.info("redis value after key: {},value:{}", "key" + i, valueFromRedis);
        }

        //数据删除测试：
//        redisTemplate.delete(key);
//        valueFromRedis = opsForValue.get(key);
//        logger.info("redis value after delete: {}", valueFromRedis);
    }

}
