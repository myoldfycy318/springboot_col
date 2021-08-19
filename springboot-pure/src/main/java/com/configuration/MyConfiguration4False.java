package com.configuration;

import com.configuration.bo.Boy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyConfiguration4False
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/6/24
 */

@Configuration(proxyBeanMethods = false)
public class MyConfiguration4False {

  @Bean
  public Boy getBoy1() {
    Boy boy = new Boy("Jerry", 18);
    return boy;
  }
}
