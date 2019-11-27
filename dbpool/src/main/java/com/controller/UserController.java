package com.controller;

import com.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 *
 * @author shanmin.zhang
 * @date 2019-09-30
 **/
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping
    public Object get() {
        return userDao.queryUsers();
    }

    @GetMapping("/get")
    public Object getOne() {
        return userDao.selectByUserId(1);
    }
}
