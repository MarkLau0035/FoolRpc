package cn.llynsyw.rpc.server;

import cn.llynsyw.rpc.common.ReflectionUtils;
import cn.llynsyw.rpc.common.RegistryUtils;
import cn.llynsyw.rpc.proto.Request;
import cn.llynsyw.rpc.proto.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 服务管理中心
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
@Slf4j
public class ServiceManager {
	private Map<ServiceDescriptor, ServiceInstance> service;

	public ServiceManager() {
		this.service = new ConcurrentHashMap<>();
	}

	/**
	 * 注册服务
	 *
	 * @param interfaceClass 注册的服务接口
	 * @param bean           接口实现类
	 **/
	public <T> void register(Class<T> interfaceClass, T bean) {
		Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);
		for (Method method : methods) {
			ServiceInstance sis = new ServiceInstance(bean, method);
			ServiceDescriptor sdp = RegistryUtils.serviceRegister(interfaceClass, method);
			service.put(sdp, sis);
			log.info("register service: {} {}", sdp.getClazz(), sdp.getMethodName());
		}
	}

	public ServiceInstance lookup(Request request) {
		ServiceDescriptor sdp = request.getServiceDescriptor();
		return service.get(sdp);
	}
}
