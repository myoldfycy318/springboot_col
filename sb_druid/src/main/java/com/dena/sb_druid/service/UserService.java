package com.dena.sb_druid.service;

import com.dena.sb_druid.entity.User;

import java.util.List;

/**
 * Created by shanmin.zhang
 * Date: 18/6/14
 * Time: 下午3:33
 */
public interface UserService {


    List<User> queryUsers();
}
