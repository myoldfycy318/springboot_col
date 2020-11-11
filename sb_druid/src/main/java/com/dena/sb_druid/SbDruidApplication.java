package com.dena.sb_druid;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@ServletComponentScan
@MapperScan({"com.dena.sb_druid.dao"})
@Slf4j
public class SbDruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbDruidApplication.class, args);
    }


    /**
     *
     * 安全优雅地关闭web请求
     * 这里主要针对SpringBoot内置的Tomcat容器，不过思路都是一样的。
     * 主要思路就是获取Tomcat的Connector连接器，然后通过Connector获取其连接线程池，最终通过操作线程池安全终止来达到web请求也安全终止的目的。
     *
     * 链接：https://www.jianshu.com/p/dc54f0bbe2a6
     *
     *
     *
     * springboot项目优雅的停止服务
     *
     * 用于接受 shutdown 事件
     */
    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addConnectorCustomizers(gracefulShutdown());
        return tomcatServletWebServerFactory;
    }

    private class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
        private volatile Connector connector;
        private final int waitTime = 10;

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        @Override
        public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
            this.connector.pause();
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            try {
                if (executor instanceof ThreadPoolExecutor) {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                        log.warn("Tomcat 进程在" + waitTime + " 秒内无法结束，尝试强制结束");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

    }
}
