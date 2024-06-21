package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 定义切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pointcut(){}

    /**
     * 定义前置通知
     */
    @Before("pointcut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始公共字段自动填充...");

        //1.获取mapper方法上面AutoFill注解的值的类型(即数据库操作的类型)
        MethodSignature signature = ( MethodSignature)joinPoint.getSignature();  //获取方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); //获取方法上面的的注解
        OperationType operationType = autoFill.value();                          //获取注解的属性

        //2.获取方法中的参数
        Object[] args = joinPoint.getArgs();
        if(args ==null || args.length==0){
            return;
        }
        Object entity = args[0];      //获取参数列表中的第一个参数

        //3.准备好属性的值
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //4.根据数据库操作类型对相应的属性赋值
        if(operationType==OperationType.INSERT){        //插入数据操作
            try {
                //获取set方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //为属性赋值
                setCreateTime.setAccessible(true);
                setCreateTime.invoke(entity,now);
                setCreateUser.setAccessible(true);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.setAccessible(true);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.setAccessible(true);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(operationType==OperationType.UPDATE){

            try {
                //获取set方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //为属性赋值

                setUpdateTime.setAccessible(true);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.setAccessible(true);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
