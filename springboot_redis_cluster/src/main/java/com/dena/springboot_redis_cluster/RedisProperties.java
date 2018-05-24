package com.dena.springboot_redis_cluster;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by shanmin.zhang
 * Date: 18/5/10
 * Time: 下午7:30
 */
@Component
@ConfigurationProperties(prefix = "xyy.redis.pool")
@PropertySource("classpath:redis.properties")
public class RedisProperties {

    /**
     * redis集群节点
     */
    private String nodes;
    /**
     * 连接超时时间
     */
    private int timeout;
    /**
     * 重连次数
     */
    private int maxAttempts;

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

}