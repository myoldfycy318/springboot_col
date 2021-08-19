package com.dena.sb_druid;

import com.dena.sb_druid.model.DataCache;
import com.dena.sb_druid.model.RandomIntGenerator;
import com.dena.sb_druid.model.NotEligibleForAutoProxyRandomIntProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * NotEligibleForAutoProxyingIntegrationTest
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/5/30
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {NotEligibleForAutoProxyRandomIntProcessor.class, DataCache.class, RandomIntGenerator.class})
public class NotEligibleForAutoProxyingIntegrationTest {

  private NotEligibleForAutoProxyRandomIntProcessor randomIntProcessor;

  @Autowired
  private DataCache dataCache;

  @Test
  public void givenAutowireInBeanPostProcessor_whenSpringContextInitialize_thenNotEligibleLogShouldShow() {
    assertEquals(0, dataCache.getGroup());
  }
}
