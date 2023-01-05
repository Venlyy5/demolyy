
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitTamplate配置
 */
@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {

    /**-------------------------------------------------
     * 全局生产者回执
     *      每个RabbitTemplate只能配置一个ReturnCallback
     *      通过SpringIOC容器上下文对象，获取RabbitTamplate对象
     *      设置全局处理生产者回执：消息通过交换机路由队列异常通知生产者
     *
     * @param context SpringIOC容器上下文对象
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        //1.获取RabbitTemplate对象
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        //2.设置全局生产者回执回调 一旦该回调被执行 则说明路由消息到队列失败
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

                // 如果是延迟消息, 则忽略错误(延迟交换机会把消息暂存,导致全局生产者回执立即返回失败, 但是延迟信息还是会被正常投递)
                if(message.getMessageProperties().getReceivedDelay()>0){
                    return;
                }

                log.error("全局生产者回执: 交换机路由消息到队列失败，消息发送失败，应答码={}，原因={}，交换机={}，路由键={}，消息={}",
                        replyCode, replyText, exchange, routingKey, message.toString());

                //3.实现其他的补救业务,例如通知运维...
            }
        });
    }


    /**--------------------------------------
     * 交换机持久化
     * 三个参数：交换机名称，是否持久化，当没有queue与其绑定时是否自动删除
     */
    @Bean
    public DirectExchange simpleExchange(){
        return new DirectExchange("simple.direct",true,false);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topic.exchange",true,false);
    }

    /**-----------------------------------
     * 队列持久化
     */
    @Bean
    public Queue simpleQueue(){
        // 使用QueueBuilder构建队列，durable就是持久化的
        return QueueBuilder.durable("simple.queue").build();
    }

}
