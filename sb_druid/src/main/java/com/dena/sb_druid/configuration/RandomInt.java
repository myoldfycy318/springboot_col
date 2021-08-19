package com.dena.sb_druid.configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * RandomInt
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/5/30
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface RandomInt {
  int min();

  int max();
}
