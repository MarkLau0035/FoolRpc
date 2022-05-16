package com.FoolRpc.rpc.client.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * @author lgc
 */
@Aspect
@Component
@Slf4j
public class ExceptionHandler {

	@Pointcut(value = "execution(* com.FoolRpc.rpc.client.impl..*.*(..))")
	public void clientPointcut() {
	}


	@Pointcut(value = "execution(* com.FoolRpc.rpc.registry.service..*(..))")
	public void registryPointcut() {
	}

	@Pointcut(value = "execution(public * com.FoolRpc.rpc.protocol.util..*.*(..))")
	public void protocolPointcut() {
	}

	/**
	 * aop异常日志输出
	 */
	@AfterThrowing(value = "clientPointcut()||registryPointcut()||protocolPointcut()",
			throwing = "exception")
	public void handleException(JoinPoint jp, Exception exception) {
		log.error("{}:{} at {}", exception.getClass().getName(), exception.getMessage(), jp.getSignature());
	}
}