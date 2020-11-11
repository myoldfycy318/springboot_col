package com.dena.sb_druid.aspect;

import java.lang.annotation.*;


/**
 * @author shanmin.zhang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwitchDB {


    /**
     * 是否切换为只读数据库
     *
     * @return
     */
    boolean isReadOnly() default false;


}
