package com.dfds.demolyy.WebsocketDemo.Demo_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

//自定义拦截器拦截WebSocket请求
public class MyWebSocketInterceptor implements HandshakeInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 前置拦截, 一般用来注册用户信息，绑定 WebSocketSession
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes){
        logger.info("握手前请求连接URL：" + request.getURI());
        if (!(request instanceof ServletServerHttpRequest)) return true;
        // 获取token信息
        // HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        // String userName = (String) servletRequest.getSession().getAttribute("userName");
        String userName = "LiYangYang";
        attributes.put("userName", userName);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        System.out.println("后置拦截~~");
    }
}
