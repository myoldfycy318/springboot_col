package com.dena.sb_druid.model;

import com.dena.sb_druid.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * MyBeanPostProcessor
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/5/30
 */
@Slf4j
@Component
public class MyBeanPostProcessor implements BeanPostProcessor, Ordered {

  @Autowired
  private ApplicationContext applicationContext;
  @Autowired
  private HelloService helloService;

  @PostConstruct
  public void init() {
    log.info("MyBeanPostProcessor init --->:{}",helloService.getClass());
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
