package com.configuration.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Girl
 *
 * @author shanmin.zhang
 * @description:
 * @date 2021/6/24
 */

@Getter
@AllArgsConstructor
public class Girl {
  private String name;
  private Integer age;
  private Boy boyfriend;
}
