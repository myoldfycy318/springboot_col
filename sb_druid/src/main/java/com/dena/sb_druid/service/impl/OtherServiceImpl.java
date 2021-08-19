package com.dena.sb_druid.service.impl;

import com.dena.sb_druid.dao.UserInfoMapper;
import com.dena.sb_druid.entity.UserInfo;
import com.dena.sb_druid.entity.UserPassword;
import com.dena.sb_druid.service.UserInfoService;
import com.dena.sb_druid.service.UserPasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OtherServiceImpl
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/1/28
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "caffeineCacheManager")
public class OtherServiceImpl {
  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private UserPasswordService userPasswordService;
  @Autowired
  private UserInfoMapper userInfoMapper;

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

  public void addUserInfo(UserInfo userInfo) {
    log.info("addUserInfo");
    userInfoMapper.insertSelective(userInfo);
  }

  @Cacheable(key = "#id",unless = "#result != null")
  public UserInfo getById(Integer id) {
    log.info("getById:{}", id);
    return userInfoMapper.selectByPrimaryKey(id);
  }

  @CachePut(key = "#userInfo.id")
  public UserInfo updateUserInfo(UserInfo userInfo) {
    log.info("updateUserInfo");
     userInfoMapper.updateByPrimaryKey(userInfo);
     return userInfo;
  }

  @CacheEvict(key = "#id")
  public void deleteById(Integer id) {
    log.info("delete");
    userInfoMapper.deleteByPrimaryKey(id);
  }

}
