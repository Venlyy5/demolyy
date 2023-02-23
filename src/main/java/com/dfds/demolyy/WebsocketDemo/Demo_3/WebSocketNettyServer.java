package com.dfds.demolyy.WebsocketDemo.Demo_3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketNettyServer {
    public static void main(String[] args) {
        // 创建两个线程池: 主线程池, 从线程池
        NioEventLoopGroup mainGrp = new NioEventLoopGroup();
        NioEventLoopGroup subGrp = new NioEventLoopGroup();

        try {
            // 创建Netty服务器启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 初始化服务器启动对象
            serverBootstrap
                    .group(mainGrp, subGrp)  //指定两个线程池
                    .channel(NioServerSocketChannel.class) //指定Netty通道类型
                    .childHandler(new WebSocketChannelInitializer()); //指定通道初始化器用来加载当Channel收到事件消息后,如何进行业务处理

            // 绑定服务器端口, 以同步的方式启动服务器
            ChannelFuture future = serverBootstrap.bind(9090).sync();
            // 等待服务器关闭
            future.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 优雅的关闭两个线程池
            mainGrp.shutdownGracefully();
            subGrp.shutdownGracefully();
        }
    }
}
