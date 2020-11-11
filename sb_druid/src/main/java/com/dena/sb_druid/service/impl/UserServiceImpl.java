package com.dena.sb_druid.service.impl;

import com.dena.sb_druid.aspect.SwitchDB;
import com.dena.sb_druid.dao.UserMapper;
import com.dena.sb_druid.entity.User;
import com.dena.sb_druid.service.UserService;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by shanmin.zhang
 * Date: 18/6/14
 * Time: 下午3:33
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryUsers() {

        ((UserServiceImpl) AopContext.currentProxy()).start();
//        start();
        return userMapper.queryUsers();
    }


    //    @Transactional
    @SwitchDB()
    public void start() {
        System.out.println("------");
    }

}
