package com.dena.sb_druid.service.impl;

import com.dena.sb_druid.dao.UserMapper;
import com.dena.sb_druid.entity.User;
import com.dena.sb_druid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shanmin.zhang
 * Date: 18/6/14
 * Time: 下午3:33
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> queryUsers() {
        return userMapper.queryUsers();
    }

}
