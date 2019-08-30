package com.compent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ConditionalTest
 *
 * @author shanmin.zhang
 * @date 2019-08-30
 **/
@Component
@ConditionalOnProperty(value = "conditional.test")
@Slf4j
public class ConditionalTest implements InitializingBean {

    public void ConditionalTest() {
        log.info("ConditionalTest init");
        System.out.println("ConditionalTest init....");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("初始化 ConditionalTest 。。。。");
    }
}
