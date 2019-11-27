package com;

import com.alibaba.fastjson.JSONObject;
import com.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbpoolApplicationTests {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        int currentPage = 2;
        int pageSize = 2;
        PageHelper.startPage(currentPage, pageSize);
        PageInfo pageInfo = PageInfo.of(userMapper.queryUsers());
        System.out.println(pageInfo.getTotal());
        pageInfo.getList().stream().forEach((item) -> System.out.println(JSONObject.toJSONString(item)));
    }



    @Test
    public void testRedis() {
        String key = "key1";
        redisTemplate.execute(action -> action.set(key.getBytes(), "v2".getBytes(), Expiration.persistent(), RedisStringCommands.SetOption.UPSERT), true);
    }


}
