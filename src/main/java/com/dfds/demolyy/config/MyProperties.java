package com.dfds.demolyy.config;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "ymllist")
//@Validated //校验属性
public class MyProperties {
    // yml 的list集合测试
    private List<String> listName = new ArrayList<>();
}
