package com.dfds.demolyy.utils.otherUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Runnable不受Spring容器管理, 无法使用@Autowired, 需要手动注入bean
 * demoService = SpringContextUtils.getApplicationContext().getBean(demoService.class);
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static final ConcurrentHashMap<String, Object> SERVICE_FACTORY = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextHolder.class);

    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanByName(String name) throws BeansException {
        return (T)applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 根据name判断是否存在指定bean
     * @param name
     * @return
     */
    public static Boolean hasBean(String name){
        return getApplicationContext().containsBean(name);
    }

    // public static <T> T getService(Class<T> cls) {
    //     String clsName = cls.getName();
    //     T v = (T)SERVICE_FACTORY.get(clsName);
    //     if (v == null) {
    //         synchronized (clsName) {
    //             v = (T)SERVICE_FACTORY.get(clsName);
    //             if (v == null) {
    //                 logger.info("*****Autowire {}*****", cls);
    //                 v = DubboContext.getService(cls);
    //                 logger.info("*****{} Autowired*****", cls);
    //                 SERVICE_FACTORY.put(clsName, v);
    //             }
    //         }
    //     }
    //     return v;
    // }
}
