package com.foolrpc.rpc.server.impl;

import com.foolrpc.rpc.common.bean.ServiceInstance;
import com.foolrpc.rpc.common.exception.RegisterFailException;
import com.foolrpc.rpc.common.util.ReflectionUtils;
import com.foolrpc.rpc.protocol.ServiceDescriptor;
import com.foolrpc.rpc.protocol.util.ProtoUtils;
import com.foolrpc.rpc.registry.service.ServiceManager;
import com.foolrpc.rpc.server.RpcServer;
import com.foolrpc.rpc.server.config.RpcServerConfig;
import com.foolrpc.rpc.transport.RequestHandler;
import com.foolrpc.rpc.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ComponentScan({"com.foolrpc.rpc.registry"})
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
	@Qualifier("nettyServerRequestHandler")
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
