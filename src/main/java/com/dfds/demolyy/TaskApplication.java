package com.dfds.demolyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TaskApplication {
    public static void main(String[] args) {
        System.out.println("程序已启动");
        SpringApplication.run(TaskApplication.class, args);
    }
}
