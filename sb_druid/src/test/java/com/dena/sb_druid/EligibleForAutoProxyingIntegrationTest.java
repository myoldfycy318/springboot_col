package com.dena.sb_druid;

import com.dena.sb_druid.model.DataCache;
import com.dena.sb_druid.model.EligibleForAutoProxyRandomIntProcessor;
import com.dena.sb_druid.model.NotEligibleForAutoProxyRandomIntProcessor;
import com.dena.sb_druid.model.RandomIntGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * NotEligibleForAutoProxyingIntegrationTest
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/5/30
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EligibleForAutoProxyRandomIntProcessor.class, DataCache.class, RandomIntGenerator.class})
public class EligibleForAutoProxyingIntegrationTest {

  private EligibleForAutoProxyRandomIntProcessor randomIntProcessor;

  @Autowired
  private DataCache dataCache;

  @Test
  public void givenAutowireInBeanPostProcessor_whenSpringContextInitialize_thenNotEligibleLogShouldShow() {
    assertNotEquals(0, dataCache.getGroup());
  }
}
