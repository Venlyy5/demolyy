package com.dfds.demolyy.ValidatorTest.annotation;

import com.dfds.demolyy.ValidatorTest.validator.RegexValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 正则校验注解
 * 注解是通过反射获取的，可以通过 Class 对象的 isAnnotationPresent()方法判断它是否应用了 某个注解，再通过 getAnnotation()方法获取 Annotation 对象
 * 要使注解能正常工作，还需要使用元注解，它是可以注解 到注解上的注解。元标签有@Retention，@Documented，@Target，@Inherited，@Repeatable 五种
 * @Retention 说明注解的存活时间，取值有 RetentionPolicy.SOURCE 注解只在源码阶段保留， 在编译器进行编译时被丢弃；RetentionPolicy.CLASS 注解只保留到编译进行的时候，并不会 被加载到JVM中。 RetentionPolicy.RUNTIME可以留到程序运行的时候，它会被加载进入到JVM 中，所以在程序运行时可以获取到它们。
 * @Documented 注解中的元素包含到 javadoc 中去
 * @Target 限 定 注 解 的 应 用 场 景 ， ElementType.FIELD 给 属 性 进 行 注 解 ； ElementType.LOCAL_VARIABLE 可以给局部变量进行注解；ElementType.METHOD 可以给方法 进行注解；ElementType.PACKAGE 可以给一个包进行注解 ElementType.TYPE 可以给一个类型 进行注解，如类、接口、枚举
 * @Inherited 若一个超类被@Inherited 注解过的注解进行注解，它的子类没有被任何注解应用 的话，该子类就可继承超类的注解
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegexValidator.class)
public @interface RegexValid {

    String regexp();

    String message() default "不匹配正则表达式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
