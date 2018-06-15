package com.dena.sb_druid;

/**
 * Created by shanmin.zhang
 * Date: 18/6/13
 * Time: 下午11:48
 */

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 过滤不需要监控的后缀
 * 配置监控拦截器
 * druid监控拦截器
 * @ClassName: DruidStatFilter
 */
@WebFilter(filterName="druidWebStatFilter",
        urlPatterns="/*",
        initParams={
                @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"),// 忽略资源
        })
public class DruidStatFilter extends WebStatFilter {

}
