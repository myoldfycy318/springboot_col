package com.dena.sb_druid;

import com.alibaba.fastjson.JSONObject;
import com.dena.sb_druid.dao.UserMapper;
import com.dena.sb_druid.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SbDruidApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {

        List<User> list = userMapper.queryUsers();
        System.out.println(JSONObject.toJSONString(list));

    }




}
