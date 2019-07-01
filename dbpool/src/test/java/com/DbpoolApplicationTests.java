package com;

import com.alibaba.fastjson.JSONObject;
import com.dao.UserMapper;
import com.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbpoolApplicationTests {


    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        int currentPage = 2;
        int pageSize = 2;
        PageHelper.startPage(currentPage, pageSize);
        PageInfo pageInfo = PageInfo.of(userMapper.queryUsers());
        System.out.println(pageInfo.getTotal());
        pageInfo.getList().stream().forEach((item) -> System.out.println(JSONObject.toJSONString(item)));
    }


    @Autowired
//    private RedisTemplate redisTemplate;

    @Test
    public void testRedis(){

    }


}
