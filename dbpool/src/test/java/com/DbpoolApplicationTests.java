package com;

import com.alibaba.fastjson.JSONObject;
import com.dao.UserMapper;
import com.entity.User;
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
        List<User> list = userMapper.queryUsers();
        list.stream().forEach((item) -> System.out.println(JSONObject.toJSONString(item)));
    }


}
