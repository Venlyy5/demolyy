package com.dfds.demolyy.NettyDemo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
 
import java.net.InetSocketAddress;
 
public class EchoClient {
    private final static String HOST = "localhost";
    private final static int PORT = 8080;
 
    public static void start() {
        // 客户端只需一个EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                // 指定Channel为 客户端端NIO模型
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(HOST, PORT))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new EchoClientHandler());
                    }
                });

        // 连接，可以通过addListener()监听到连接是否成功
        try {
            ChannelFuture f = bootstrap.connect().addListener(future -> {
                if (future.isSuccess()){
                    System.out.println("Connect Success!");
                }else {
                    System.out.println("Connect Fail!");
                }
            }).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
 
    public static void main(String[] args) {
        start();
    }
}