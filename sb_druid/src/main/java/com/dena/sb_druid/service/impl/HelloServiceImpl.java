package com.dena.sb_druid.service.impl;

import com.dena.sb_druid.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * HelloServiceImpl
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/5/30
 */

@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

  @Async
  @Override
  public Object hello() {
    log.info("当前线程：" + Thread.currentThread().getName());
    return "service hello";
  }
}
