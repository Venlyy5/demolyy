package com.dfds.demolyy.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ApplicationRunner 和 CommandLineRunner 的区别就是封装参数的形式不一样，ApplicationRunner将参数封装到ApplicationArguments类中，而CommandLineRunner 将参数传到String可变数组中
 * ApplicationRunner将参数封装到ApplicationArguments类中，可以解析“--key=value”格式的参数，既可以获取参数的字符串，也可以直接获取key；
 * CommandLineRunner将参数传到String可变数组中，只能获取参数的字符串。
 *
 * 在applicationContext容器加载完成之后，会调用SpringApplication类的callRunners(),
 * 该方法中会获取所有实现了ApplicationRunner和CommandLineRunner的接口bean，然后依次执行对应的run方法，并且是在同一个线程中执行。
 * 因此如果有某个实现了ApplicationRunner接口的bean的run方法一直循环不返回的话，后续的代码将不会被执行。
 */
@Slf4j
//@Component
//@Order(value = 2)
public class ApplicationRunJob implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("应用启动预加载ApplicationRunner: {}, {}", args.getSourceArgs(), args.getOptionNames());
    }
}
