
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 测试： Rabbit消费者监听器 - 接受搜索微服务的消息
 */
@Component
@Slf4j
public class RabbitTestListener {

    /**---------------------------------------------
     * 普通监听器
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "simple.queue"
                    ),
                    exchange = @Exchange(
                            value = "simple.topic.exchange",
                            type = "topic"
                    ),
                    key = "*.*"
            )
    )
    public void listenSimpleQueue(String msg) {
        log.info("普通监听器 - 接收到simple.queue的消息：{}", msg);
        // int i = 1 / 0; //模拟消费者异常
    }


  /**----------------------------------------------------
   * 失败重试策略：用于接收那些 重试依然失败业务消息，交给人工处理
   */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "error.queue"
                    ),
                    exchange = @Exchange(
                            value = "error.direct",
                            type = "direct"
                    ),
                    key = "error"
            )
    )
    public void listenErrorQueue(String msg) {
        log.info("异常信息监听器 - 将业务消息存入数据库，由人工处理。错误消息：{}",msg);
    }


    /**-----------------------------------------------------------
     * 死信监听器 - 消费者处理死信
     * 指明死信队列, 死信交换机, RoutingKey
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dl.ttl.queue", durable = "true"),
            exchange = @Exchange(name = "dl.ttl.exchange"),
            key = "dl"
    ))
    public void listenDlQueue(String msg){
        log.info("死信监听器 - 接收到 dl.ttl.queue的延迟消息：{}", msg);
    }


    /**--------------------------------------------------------
     * DelayExchange插件实现延迟队列 - 延迟交换机监听器
     * 必须先安装该MQ插件,不然MQ无法识别 x-delayed-message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue", durable = "true"),
            exchange = @Exchange(name = "delay.exchange", delayed = "true"),
            key = "delay"
    ))
    public void listenDelayQueue(String msg){
        log.info("延迟监听器 - 接收到延迟消息：{}", msg);
    }

    /**-------------------------------------------------------
     * 惰性队列监听器
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "lazy.queue",
                    durable = "true",
                    arguments = @Argument(name = "x-queue-mode",value = "lazy")),
            exchange = @Exchange(name = "lazy.exchange"),
            key = "lazy"
    ))
    public void listenLazyQueue(String msg) throws InterruptedException {
        log.info("惰性队列 - 接收到lazy.queue的延迟消息：{}", msg);
    }

}
