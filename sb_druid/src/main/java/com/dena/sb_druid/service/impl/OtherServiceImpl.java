package com.dena.sb_druid.service.impl;

import com.dena.sb_druid.entity.UserInfo;
import com.dena.sb_druid.entity.UserPassword;
import com.dena.sb_druid.service.UserInfoService;
import com.dena.sb_druid.service.UserPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OtherServiceImpl
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/1/28
 */
@Service
public class OtherServiceImpl {
  @Autowired private UserInfoService userInfoService;
  @Autowired private UserPasswordService userPasswordService;

  @Transactional
  public void testTransactional() {

    UserInfo userInfo = new UserInfo();
    userInfo.setAge(11);
    userInfo.setName("zz");

    UserPassword pw = new UserPassword();
    pw.setUserId(1);
    pw.setEncryptPassword("x");

    userInfoService.saveUserInfo(userInfo);
    userPasswordService.savePW(pw);
    throw new RuntimeException("抛异常");
  }
}
