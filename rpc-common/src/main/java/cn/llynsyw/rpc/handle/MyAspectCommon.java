package cn.llynsyw.rpc.handle;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lgc
 */
@Slf4j
@Aspect
@Component
public class MyAspectCommon {
    /**
     * aop异常日志输出
     */
    @AfterThrowing(value = "execution(* cn.llynsyw.rpc..*.*(..))",
            throwing = "ex")
    public void myAfterThrowing(JoinPoint jp, Exception ex){
        log.info(new Date() +"MyAspectCommon发生异常，异常信息" + ex.toString() +
        "异常方法：" + jp.getSignature());
    }
}