package cn.llynsw.rpc.handle;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * @author lgc
 */
@Slf4j
@Aspect
@Component
public class MyAspectClient {
    /**
     * aop异常日志输出
     */
    @AfterThrowing(value = "execution(* cn.llynsw.rpc..*.*(..))",
            throwing = "ex")
    public void myAfterThrowing(JoinPoint jp, Exception ex) {
        Object[] args = jp.getArgs();
        log.info("MyAspectClient捕获异常:异常时间-异常方法-异常信息-调用参数\n" +
                "{" + new Date() + "}\n" +
                "{" + jp.getSignature() + "}\n" +
                "{" + ex.toString() + "}\n" +
                "{" + Arrays.asList(args) + "}");
    }
}