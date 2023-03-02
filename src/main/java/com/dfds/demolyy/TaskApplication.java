package com.dfds.demolyy;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashSet;
import java.util.Set;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class TaskApplication {
    public static void main(String[] args) {

        // 方式1.通过静态方法直接运行, 传入的class，不一定是运行类的class，也可以是自己定义的class，只要有@SpringBootApplication注解即可
        //SpringApplication.run(ApplicationConfiguration.class, args);

        // 方式2.通过SpringApplication调整
        SpringApplication springApplication = new SpringApplication();
        springApplication.setAddCommandLineProperties(false); // 禁用命令行属性
        //springApplication.setBannerMode(Banner.Mode.OFF); //Banner模式:关闭
        //springApplication.setWebApplicationType(WebApplicationType.NONE); //不创建Web
        springApplication.setAdditionalProfiles("dev"); //激活application-{profile}.properties/yml配置
        springApplication.setHeadless(true);
        // 配置Class 名称
        Set<String> sources = new HashSet();
        sources.add(ApplicationConfiguration.class.getName());
        springApplication.setSources(sources);
        springApplication.run(args);


        // 方式3.通过SpringApplicationBuilder调整
        //new SpringApplicationBuilder(TaskApplication.class)
        //        .bannerMode(Banner.Mode.CONSOLE)
        //        .web(WebApplicationType.NONE)
        //        .profiles("dev")
        //        .headless(true)
        //        .run(args);
    }

    @SpringBootApplication
    public static class ApplicationConfiguration {
    }
}
