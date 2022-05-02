package cn.llynsyw.rpc.registry.service.impl;

import cn.llynsyw.rpc.common.util.ReflectionUtils;
import cn.llynsyw.rpc.common.bean.ServiceInstance;
import cn.llynsyw.rpc.protocol.Request;
import cn.llynsyw.rpc.protocol.ServiceDescriptor;
import cn.llynsyw.rpc.protocol.util.ProtoUtils;
import cn.llynsyw.rpc.registry.service.ServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务管理中心
 *
 * @author luolinyuan
 * @date 2022/3/29
 **/
@Slf4j
@Component
@ConditionalOnProperty(prefix = "rpc",name = "connectMod",havingValue = "direct")
public class ServiceDirectConnectManagerImpl implements ServiceManager {
	private Map<ServiceDescriptor, ServiceInstance> service;

	public ServiceDirectConnectManagerImpl() {
		this.service = new ConcurrentHashMap<>();
	}

	@Override
	public <T> void register(Class<T> interfaceClass, T bean, String host, int port) {
		log.info("try to register service: at {}:{}", host, port);
		try {
			Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);
			for (Method method : methods) {
				ServiceInstance sis = new ServiceInstance(bean, method);
				ServiceDescriptor sdp = ProtoUtils.generateServiceDescriptor(interfaceClass, method);
				service.put(sdp, sis);
				log.info("register service: {} {}", sdp.getClazz(), sdp.getMethodName());
			}
		} catch (Exception e) {
			throw new IllegalStateException("register service:" + interfaceClass.getName() + "fail");
		}
	}

	@Override
	public ServiceInstance lookup(Request request) {
		ServiceDescriptor sdp = request.getServiceDescriptor();
		return service.get(sdp);
	}
}
