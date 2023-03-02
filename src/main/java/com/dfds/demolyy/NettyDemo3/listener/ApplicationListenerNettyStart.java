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
import java.util.concurrent.*;

/**
 * 自定义监听器需要实现ApplicationListener接口，实现对应的方法来完成自己的业务逻辑。
 *
 * [SpringBoot Application共支持6种事件监听，按顺序分别是]
 * 1.ApplicationStartingEvent：在Spring最开始启动的时候触发，此时除了注册监听器和初始器之外，其他所有处理都没有开始
 * 2.ApplicationEnvironmentPreparedEvent：在Spring已经准备好上下文但是上下文尚未创建的时候触发,可加载一些属性
 * 3.ApplicationPreparedEvent：在Bean定义加载之后(此时bean还没有初始化)、刷新上下文之前触发
 * 4.ApplicationStartedEvent：在刷新上下文之后、调用application命令之前触发
 * 5.ApplicationReadyEvent：在调用application命令之后触发(所有bean初始化完毕，所有回调处理完成)
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
 *
 *
 * execute()方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功与否
 * submit()方法用于提交需要返回值的任务。线程池会返回一个Future类型的对象，通过这个Future对象可以判断任务是否执行成功，并且可以通过Future的get()方法来获取返回值，get()方法会阻塞当前线程直到任务完成，而使用get(long timeout,TimeUnit unit)方法则会阻塞当前线程一段时间后立即返回，这时候有可能任务没有执行完。
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
        ExecutorService executorService = new ThreadPoolExecutor(
                10,
                15,
                1000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new DefaultThreadFactory("nettyPoolName"),
                new ThreadPoolExecutor.CallerRunsPolicy()); //默认拒绝策略是AbortPolicy
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
        Future<?> submit = executorService.submit(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                InetSocketAddress address = new InetSocketAddress(socketProperties.getHost(), 503);
                coordinationNettyServer.start(address);
            }
        });
    }
}
