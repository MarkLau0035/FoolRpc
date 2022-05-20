package com.foolrpc.rpc.server.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * @author lgc
 */
@Slf4j
@Aspect
@Component
public class ExceptionHandler {
	@Pointcut(value = "execution(public * com.foolrpc.rpc.server.impl..*.*(..))")
	public void serverPointcut() {
	}


	@Pointcut(value = "execution(public * com.foolrpc.rpc.registry.service..*.*(..))")
	public void registryPointcut() {
	}

	@Pointcut(value = "execution(public * com.foolrpc.rpc.protocol.util..*(..))")
	public void protocolPointcut() {
	}

	/**
	 * aop异常日志输出
	 */
	@AfterThrowing(value = "serverPointcut()||registryPointcut()||protocolPointcut()",
			throwing = "exception")
	public void handleException(JoinPoint jp,Exception exception) {
		log.error("{}:{} from {}", exception.getClass().getName(),exception.getMessage(),jp.getSignature());
	}
}