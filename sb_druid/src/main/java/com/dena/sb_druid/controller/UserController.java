package com.dena.sb_druid.controller;

import com.dena.sb_druid.entity.User;
import com.dena.sb_druid.service.HelloService;
import com.dena.sb_druid.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by shanmin.zhang
 * Date: 18/6/14
 * Time: 下午3:32
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private HelloService helloService;


  @GetMapping("all")
  public List<User> queryUsers() {
    return userService.queryUsers();
  }

  @Async
  @GetMapping("async")
  public Object hello() {
    return helloService.hello();
  }
}
