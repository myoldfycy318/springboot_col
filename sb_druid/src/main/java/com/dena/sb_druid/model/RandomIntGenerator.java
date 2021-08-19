package com.dena.sb_druid.model;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * RandomIntGenerator
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/5/30
 */

@Component
public class RandomIntGenerator {
  private Random random = new Random();
  private DataCache dataCache;

  public RandomIntGenerator(DataCache dataCache) {
    this.dataCache = dataCache;
  }

  public int generate(int min, int max) {
    return random.nextInt(max - min) + min;
  }
}
