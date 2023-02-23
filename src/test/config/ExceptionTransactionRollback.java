package com.heima.common.advice;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * 用来处理程序调用异常，由于被全局异常处理后，导致分布式事务失效
 * 解决办法:
 *  1.让分布式事务请求的接口不被全局异常拦截(@RestControllerAdvice配置basePackages, 独立配置分布式事务的接口)
 *  2.基于AOP思想, @AfterThrowing 当异常发生时, 如果该业务被全局事务管理, 则手动执行事务的回滚.
 * AfterThrowing处理虽然处理了该异常，但它不能完全处理异常，该异常依然会抛到上一级调用者，即JVM
 *
 * 提取到common中,运行时会自动加载seata依赖,导致未配置seata的服务异常
 * 解决办法1.所以要更改Maven依赖作用范围: <scope>provided</scope>
 * 解决办法2.使用注解可以有条件的加载bean:
 *  @Conditional: 可以标注在类上面，表示该类下面的所有@Bean都会启用配置，也可以标注在方法上面，只是对该方法启用配置。
 *  @ConditionalOnBean: 仅仅在当前上下文中存在某个对象时，才会实例化一个Bean
 *  @ConditionalOnClass: 某个class位于类路径上，才会实例化一个Bean
 *  @ConditionalOnExpression: 当表达式为true的时候，才会实例化一个Bean
 *  @ConditionalOnMissingBean: 仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean
 *  @ConditionalOnMissingClass: 某个class类路径上不存在的时候，才会实例化一个Bean
 *  @ConditionalOnNotWebApplication: 不是web应用
 */
@Slf4j
@Aspect  // 切面类
@Component
@ConditionalOnClass(RootContext.class)  // 属于io.seata.core.context, 工程中引用了Seata包 才会构建这个bean
public class ExceptionTransactionRollback {
    /**
     * AOP的五个重要注解:
     *              1.@Before: 目标方法执行之前
     *              2.@After: 目标方法执行完，无论是否出现错误异常，都会执行
     *              3.@AfterReturning: 目标方法返回结果时，出现错误异常，不会执行
     *              4.@AfterThrowing: 目标方法出现异常时执行
     *              5.@Around: 可以实现以上所有功能
     *
     * 切面：将增强作用到切入点
     * pointcut 切入点 对业务层进行增强
     * pointcut = "execution(任意返回值 包名.任意子包.任意方法(任意参数))"
     */
    @AfterThrowing(throwing = "e", pointcut = "execution(* com.heima.*.service.*.*(..))")
    public void rollback(Throwable e) throws Exception {
        log.error("分布式事务执行异常，执行全局事务回滚,{}", e.getMessage());

        // 1.根据全局事务的XID, 判断当前业务方法是否加入到全局事务中（是否为分布式事务管理）
        String xid = RootContext.getXID();
        if (StringUtils.isNotBlank(xid)) {
            // 2.手动执行事务回滚, 该异常依然会向上抛
            GlobalTransactionContext.reload(xid).rollback();
        }
    }
}