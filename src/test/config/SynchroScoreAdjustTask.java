package com.uhome.cloud.db.task;

import com.uhome.cloud.db.config.redisson.RedissLockUtil;
import com.uhome.cloud.db.rest.MbScoreGoodsRest;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 积分调整接口定时同步
 * @Author liuguicheng
 * @create 2020/03/20 15:30
 */
@Component
@Slf4j
public class SynchroScoreAdjustTask implements SchedulingConfigurer {
    private  String  CRON="0 */1 * * * ?"; //每分钟执行一次
    private static final String synchroScoreAdjustTask="synchroScoreAdjustTask";
    @Autowired
    private MbScoreGoodsRest mbScoreGoodsRest;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            refreshTask();
        }, triggerContext -> {
            // 任务触发，可修改任务的执行周期
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }

    //使用Redisson实现分布式锁
    protected void refreshTask() {
        long startTime=System.currentTimeMillis();
        log.warn("【积分调整定时同步】定时任务启动 时间【{}】",startTime);
        RLock lock =  RedissLockUtil.getLock(synchroScoreAdjustTask);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(0, 20, TimeUnit.SECONDS)) {
                mbScoreGoodsRest.refreshHdScoreAdd();
            } else {
                log.warn("【积分调整定时同步】分布式锁没有获取到锁:{},ThreadName :{}",synchroScoreAdjustTask, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("【积分调整定时同步】获取分布式锁异常",e);
        }finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            log.warn("【积分调整定时同步】分布式锁释放锁:{},ThreadName :{}", synchroScoreAdjustTask, Thread.currentThread().getName());
        }
        log.warn("【积分调整定时同步】定时任务结束 耗时【{}】毫秒",System.currentTimeMillis()-startTime);
    }


}
