package com.dena.sb_druid.service.impl;

import com.dena.sb_druid.dao.UserInfoMapper;
import com.dena.sb_druid.entity.UserInfo;
import com.dena.sb_druid.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserInfoServiceImpl
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/1/28
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

  @Autowired
  private UserInfoMapper userInfoMapper;

  @Transactional
  @Override
  public void saveUserInfo(UserInfo userInfo) {
    userInfoMapper.insertSelective(userInfo);
  }
}
