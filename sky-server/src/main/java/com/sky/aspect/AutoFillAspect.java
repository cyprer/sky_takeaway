package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 定义切点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void AutoFillPoint(){}

    @Before("AutoFillPoint()")
    public void doAutoFill(JoinPoint joinPoint){
        log.info("开始进行自动填充公共字段");

        // 获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();//获取方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value();//获取数据库操作类型
        //获取到当前被拦截的方法上的参数即实体对象
        Object[] args = joinPoint.getArgs();//获取方法参数
        if (args==null||args.length==0){
            return;
        }
        Object arg = args[0];//获取方法参数的第一个参数,约定第一个参数为实体对象
        //准备赋值的数据类型
        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();
        //根据不同的数据库操作类型，为对应的数据赋值
        if (operationType==OperationType.INSERT) {
            try {
                Method setCreateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(arg, now);
                setCreateUser.invoke(arg, userId);
                setUpdateTime.invoke(arg, now);
                setUpdateUser.invoke(arg, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                    Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                    setUpdateTime.invoke(arg, now);
                    setUpdateUser.invoke(arg, userId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("公共字段填充完成");
    }
}
