package com.FoolRpc.rpc.server.impl;

import com.FoolRpc.rpc.common.bean.ServiceInstance;
import com.FoolRpc.rpc.common.exception.RegisterFailException;
import com.FoolRpc.rpc.common.util.ReflectionUtils;
import com.FoolRpc.rpc.protocol.ServiceDescriptor;
import com.FoolRpc.rpc.protocol.util.ProtoUtils;
import com.FoolRpc.rpc.registry.service.ServiceManager;
import com.FoolRpc.rpc.server.RpcServer;
import com.FoolRpc.rpc.server.config.RpcServerConfig;
import com.FoolRpc.rpc.transport.RequestHandler;
import com.FoolRpc.rpc.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * RpcServerImpl
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/

@Slf4j
@Component
@ComponentScan({"com.FoolRpc.rpc.registry"})
public class RpcServerImpl implements RpcServer {

	private RpcServerConfig serverConfig;

	private ServiceManager serviceManager;

	private TransportServer serverNetwork;

	private RequestHandler handler;

	@Autowired
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	@Autowired
	public void setServerNetwork(TransportServer serverNetwork) {
		this.serverNetwork = serverNetwork;
	}


	@Autowired
	public void setServerConfig(RpcServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Autowired
	public void setHandler(RequestHandler handler) {
		this.handler = handler;
	}

	protected RpcServerImpl() {
	}

	@Override
	public <T> void register(Class<T> interfaceClass, T bean) {
		if (interfaceClass == null) {
			throw new RegisterFailException();
		}
		String host = serverConfig.getIp() + ":" + serverConfig.getPort();
		Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);
		Map<String, ServiceInstance> serviceMap = new HashMap<>(methods.length);
		for (Method method : methods) {
			ServiceDescriptor descriptor = ProtoUtils.generateServiceDescriptor(interfaceClass, method);
			ServiceInstance instance = new ServiceInstance(bean, method);
			serviceMap.put(descriptor.toString(), instance);
		}
		serviceManager.register(serviceMap, interfaceClass.getName(), host);
	}


	@Override
	public void start() {
		this.serverNetwork.init(serverConfig.getPort(), handler);
		this.serverNetwork.start();
	}

	@Override
	public void stop() {
		this.serverNetwork.stop();
	}
}
