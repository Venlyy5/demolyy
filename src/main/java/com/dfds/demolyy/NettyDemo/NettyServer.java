package com.dfds.demolyy.NettyDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Netty服务器
 * 功能描述: Netty服务启动类
 *
 * 说一下思路， 这里场景各种设备连接DTU，然后通过DTU上报报文，和接收服务器下发的指令。
 * 例如127.0.0.1：2233 就是你服务器的ip和端口，我们需要开发部署一个 JAVA 开发的Netty 服务器来监听 2233端口， 从机配置我们的服务器ip和端口连接到netty。
 * 那么我们开发netty 的思路应该是什么样子的。
 * 1.netty 监听端口；
 * 2.netty 保存通道长链接；
 * 3.将netty的里面的所有通道 存放到一个 ConcurrentHashMap 里面来进行管理；
 * 4.通过 netty 监听 我们可以获取 从机上报到服务器的报文，我们进行业务处理；
 * 5.通过Map 我们实现 定时下发报文，让从机回复响应；
 * 原文链接：https://blog.csdn.net/Crazy_Cw/article/details/126613967
 */
@Slf4j
@Component
public class NettyServer {
    public void start(InetSocketAddress address) {
        //配置服务端的NIO线程组,boss处理连接, worker处理数据
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 绑定线程池,编码解码
            //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    // 指定Channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(address)
                    //使用自定义处理类
                    .childHandler(new NettyServerChannelInitializer())
                    //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //保持长连接，2小时无数据激活心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //将小的数据包包装成更大的帧进行传送，提高网络的负载
                    .childOption(ChannelOption.TCP_NODELAY, true);
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(address).sync();
            if (future.isSuccess()) {
                log.info("netty服务器开始监听端口：{}",address.getPort());
            }
            //关闭channel和块，直到它被关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
