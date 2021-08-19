package com;

import com.configuration.MyConfiguration;
import com.configuration.MyConfiguration4False;
import com.configuration.bo.Boy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TestConfiguration
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/6/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestConfiguration {

  @Autowired
  BeanFactory beanFactory;

  @Test
  public void testConfiguration() {
    MyConfiguration bean = beanFactory.getBean(MyConfiguration.class);
    Boy boy1 = bean.getBoy();
    Boy boy2 = bean.getBoy();
    System.out.println("两次调用的对象是否相等？" + (boy1 == boy2));

  }


  @Test
  public void testConfiguration4False() {
    MyConfiguration4False bean1 = beanFactory.getBean(MyConfiguration4False.class);
    Boy boy1 = bean1.getBoy1();
    Boy boy2 = bean1.getBoy1();
    System.out.println("两次调用的对象是否相等？" + (boy1 == boy2));
  }


}
