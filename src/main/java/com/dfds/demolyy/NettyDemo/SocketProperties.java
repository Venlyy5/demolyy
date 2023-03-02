package com.dfds.demolyy.NettyDemo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 功能描述: 配置类
 */
@Setter
@Getter
@ToString
@Component
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "socket")
public class SocketProperties {
    //@Value("${socket.port}")
    private Integer port;
    //@Value("${socket.host}")
    private String host;
}
