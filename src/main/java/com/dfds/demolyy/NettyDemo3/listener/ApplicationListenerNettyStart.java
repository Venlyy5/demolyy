package com.dfds.demolyy.NettyDemo3.listener;

import com.dfds.demolyy.NettyDemo.SocketProperties;
import com.dfds.demolyy.NettyDemo3.server.CoordinationNettyServer;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义监听器需要实现ApplicationListener接口，实现对应的方法来完成自己的业务逻辑。
 *
 * [SpringBoot Application共支持6种事件监听，按顺序分别是]
 * 1.ApplicationStartingEvent：在Spring最开始启动的时候触发
 * 2.ApplicationEnvironmentPreparedEvent：在Spring已经准备好上下文但是上下文尚未创建的时候触发,可加载一些属性
 * 3.ApplicationPreparedEvent：在Bean定义加载之后、刷新上下文之前触发
 * 4.ApplicationStartedEvent：在刷新上下文之后、调用application命令之前触发
 * 5.ApplicationReadyEvent：在调用application命令之后触发
 * 6.ApplicationFailedEvent：在启动Spring发生异常时触发
 *
 * [注意]
 * ApplicationRunner和CommandLineRunner的执行在第五步和第六步之间
 * Bean的创建在第三步和第四步之间
 * 在启动类中，执行SpringApplication.run()方法后的代码，会在第六步后执行
 *
 * [使用场景]
 * 我们在业务场景中可能会使用到rabbitmq或者kafka等消息队列，一般我们要求在服务启动的时候就将一些配置，topic等信息加载完成，并连接上kafka。这个时候我们就可以通过实现 2. ApplicationListener<ApplicationReadyEvent>来进行，这个阶段Bean已经创建完毕，可以拿到对应autowired的对象来进行相关逻辑处理；
 * 再比如，我们需要校验某些环境，配置是否满足，如果不满足就退出程序也可以在监听器中实现；
 * 常见的数据库连接；
 * 特定定时任务的执行。
 */
//@Order(1)
//@Component
public class ApplicationListenerNettyStart implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private SocketProperties socketProperties;
    @Autowired
    private CoordinationNettyServer coordinationNettyServer;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 15,1000,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new DefaultThreadFactory("nettyPoolName"));
        // 端口502
        executorService.submit(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                InetSocketAddress address = new InetSocketAddress(socketProperties.getHost(), 502);
                coordinationNettyServer.start(address);
            }
        });
        // 端口503
        executorService.submit(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                InetSocketAddress address = new InetSocketAddress(socketProperties.getHost(), 503);
                coordinationNettyServer.start(address);
            }
        });
    }
}
