package com.dena.sb_druid.service;

import com.dena.sb_druid.entity.UserPassword;

/**
 * UserPassword
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/1/28
 */

public interface UserPasswordService {

  void savePW(UserPassword password);
}
