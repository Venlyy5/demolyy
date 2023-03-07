package com.dfds.demolyy.NettyDemo3.server;

import com.dfds.demolyy.NettyDemo.NettyServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class CoordinationNettyServer {
    public void start(InetSocketAddress address) {
        // 分包/粘包问题一般有三种处理方式：1.帧头包含后续字节数；2.帧头帧尾校验；3.使用固定报文的长度
        // Netty处理分包/粘包可以使用其自带的解码器

        //配置服务端的NIO线程组,boss处理客户端的连接, worker处理数据
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); //一般并发连接量不大时可指定线程数为1
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //线程数量默认值(CPU核心数*2)
        try {
            // 创建Server端启动引导类
            ServerBootstrap bootstrap = new ServerBootstrap()
                    // 配置两大线程组，确定线程模型
                    .group(bossGroup, workerGroup)
                    // 指定Channel为 服务端NIO模型
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(address)
                    //指定服务端消息的业务处理逻辑 自定义Handler对象
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
