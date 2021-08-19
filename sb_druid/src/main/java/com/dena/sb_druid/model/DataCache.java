package com.dena.sb_druid.model;

import com.dena.sb_druid.configuration.RandomInt;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * DataCache
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/5/30
 */
@Data
@Component
public class DataCache {
  @RandomInt(min = 2, max = 10)
  private int group;
  private String name;
}
