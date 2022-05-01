package cn.llynsyw.rpc.registry.service.impl;

import cn.llynsyw.rpc.common.bean.ServiceInstance;
import cn.llynsyw.rpc.common.util.ReflectionUtils;
import cn.llynsyw.rpc.protocol.Request;
import cn.llynsyw.rpc.protocol.ServiceDescriptor;
import cn.llynsyw.rpc.protocol.util.ProtoUtils;
import cn.llynsyw.rpc.registry.service.ServiceManager;
import cn.llynsyw.rpc.registry.zookeeper.ZkCuratorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/5/1
 **/
@Slf4j
@Component
@ConditionalOnProperty(prefix = "rpc",name = "connectMod",havingValue = "registry")
public class ServiceWithRegistryManagerImpl implements ServiceManager {

	private Map<ServiceDescriptor, ServiceInstance> service;

	private ZkCuratorClient client;


	public ServiceWithRegistryManagerImpl() {
		this.service = new ConcurrentHashMap<>();
	}

	@Autowired
	public void setClient(ZkCuratorClient client) {
		this.client = client;
	}

	@Override
	public <T> void register(Class<T> interfaceClass, T bean, String host, int port) {
		log.info("try to register service: at {}:{}", host, port);
		try {
			StringBuffer pathBf = new StringBuffer();
			pathBf.append("/")
					.append(interfaceClass.getName())
					.append("/provider")
					.append("/")
					.append(host).append(":").append(port);
			client.createEphemeral(pathBf.toString());
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
