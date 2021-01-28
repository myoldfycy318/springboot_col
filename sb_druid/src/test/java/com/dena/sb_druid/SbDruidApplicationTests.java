package com.dena.sb_druid;

import com.alibaba.fastjson.JSONObject;
import com.dena.sb_druid.dao.UserMapper;
import com.dena.sb_druid.entity.User;
import com.dena.sb_druid.entity.UserInfo;
import com.dena.sb_druid.entity.UserPassword;
import com.dena.sb_druid.service.UserInfoService;
import com.dena.sb_druid.service.UserPasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SbDruidApplicationTests {

    private Logger logger = LoggerFactory.getLogger(SbDruidApplication.class);

    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {

        List<User> list = userMapper.queryUsers();
        String str = JSONObject.toJSONString(list);

        logger.debug("debug 日志。。。,{}", str);
        logger.info("info 日志。。。,{}", str);
        logger.error("error 日志。。。,{}", str);

    }

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserPasswordService userPasswordService;
    @Test
    public void TestTrans(){

      UserInfo userInfo = new UserInfo();
      userInfo.setAge(11);
      userInfo.setName("zz");

      UserPassword pw = new UserPassword();
      pw.setUserId(1);
      pw.setEncryptPassword("x");

      userInfoService.saveUserInfo(userInfo);
      userPasswordService.savePW(pw);

    }


}
