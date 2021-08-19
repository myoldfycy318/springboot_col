package com.dena.sb_druid;

import com.alibaba.fastjson.JSONObject;
import com.dena.sb_druid.dao.UserMapper;
import com.dena.sb_druid.entity.User;
import com.dena.sb_druid.entity.UserInfo;
import com.dena.sb_druid.entity.UserPassword;
import com.dena.sb_druid.service.UserInfoService;
import com.dena.sb_druid.service.UserPasswordService;
import com.dena.sb_druid.service.impl.OtherServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SbDruidApplicationTests {


  @Autowired
  private UserMapper userMapper;

  @Test
  public void contextLoads() {

    List<User> list = userMapper.queryUsers();
    String str = JSONObject.toJSONString(list);

    log.debug("debug 日志。。。,{}", str);
    log.info("info 日志。。。,{}", str);
    log.error("error 日志。。。,{}", str);
  }

  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private UserPasswordService userPasswordService;

  @Test
  public void testTrans() {

    UserInfo userInfo = new UserInfo();
    userInfo.setAge(11);
    userInfo.setName("zz");

    UserPassword pw = new UserPassword();
    pw.setUserId(1);
    pw.setEncryptPassword("x");

    userInfoService.saveUserInfo(userInfo);
    userPasswordService.savePW(pw);
  }

  @Autowired
  private OtherServiceImpl otherServiceImpl;

  @Test
  public void testThrowException() {
    try {
      otherServiceImpl.testTransactional();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testCache() {
    UserInfo userInfo = otherServiceImpl.getById(1);
    log.info(JSONObject.toJSONString(userInfo));
    userInfo = otherServiceImpl.getById(1);
    log.info(JSONObject.toJSONString(userInfo));

    userInfo = new UserInfo();
    userInfo.setAge(21);
    userInfo.setName("xz");
    userInfo.setId(1);
    otherServiceImpl.updateUserInfo(userInfo);

    userInfo = otherServiceImpl.getById(1);
    log.info(JSONObject.toJSONString(userInfo));

  }

}
