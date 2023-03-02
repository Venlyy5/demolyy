package com.dfds.demolyy.task;

import com.dfds.demolyy.NettyDemo.DtuManage;
import com.dfds.demolyy.service.StatisticsDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

    /**
     * cron = "0 0 0 * * ?"  每天0点执行一次
     * cron = "0 0/15 * * * ?" 从第0分钟开始，每隔15分钟触发一次
     * 0 2,22,32 * * * ? 在2分、22分、32分执行一次
     * 0 0 4-8 * * ? 每天4-8点整点执行一次
     *
     * 如果不自定义异步方法的线程池默认使用SimpleAsyncTaskExecutor
     * SimpleAsyncTaskExecutor不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程。并发大的时候会产生严重的性能问题。
     */
    @Async(value = "ThreadPoolNameLyy")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void task1() throws Exception {
        //获取上一天的日期
        //String day = DateUtil_3.formatDate(DateUtil_3.addDays(new Date(), -1));
        //dailyService.test(day);

        //定时发送Dtu报文
        DtuManage dtuManage = new DtuManage();
        dtuManage.sendMsg();
        dtuManage.deleteInactiveConnections();
    }
}