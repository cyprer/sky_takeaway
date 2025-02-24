package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识需要自动填充的字段
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) //它确保了 @AutoFill 注解不仅在编译时有效，而且在程序运行时也可以被访问和使用
public @interface AutoFill {
    //数据库操作类型,UPDATE,INSERT
    OperationType value();
}
