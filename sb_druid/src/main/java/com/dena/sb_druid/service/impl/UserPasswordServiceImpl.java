package com.dena.sb_druid.service.impl;

import com.dena.sb_druid.dao.UserPasswordMapper;
import com.dena.sb_druid.entity.UserPassword;
import com.dena.sb_druid.service.UserPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserPasswordServiceImpl
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/1/28
 */
@Service
public class UserPasswordServiceImpl implements UserPasswordService {

  @Autowired
  private UserPasswordMapper mapper;

  @Transactional
  @Override
  public void savePW(UserPassword password) {
    mapper.insertSelective(password);
  }
}
