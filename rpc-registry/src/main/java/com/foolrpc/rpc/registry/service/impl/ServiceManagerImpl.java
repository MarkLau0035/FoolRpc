package com.foolrpc.rpc.registry.service.impl;

import com.foolrpc.rpc.common.bean.ServiceInstance;
import com.foolrpc.rpc.common.exception.NoAvailableServiceException;
import com.foolrpc.rpc.registry.service.ServiceManager;
import com.foolrpc.rpc.registry.zookeeper.ZkCuratorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceManagerImpl
 *
 * @author luolinyuan
 * @date 2022/5/17
 **/
@Component
@Slf4j
public class ServiceManagerImpl implements ServiceManager {
	private final Map<String, ServiceInstance> serverCaches;

	private ZkCuratorClient zkClient;

	public ServiceManagerImpl() {
		this.serverCaches = new ConcurrentHashMap<>();
	}

	@Autowired(required = false)
	public void setZkClient(ZkCuratorClient zkClient) {
		this.zkClient = zkClient;
	}


	@Override
	public <T> void register(Map<String, ServiceInstance> serviceMap, String className, String host) {
		serverCaches.putAll(serviceMap);
		if (this.zkClient != null) {
			String znodePath = "/" + className + "/provider";
			try {
				if (!zkClient.checkExists(znodePath)) {
					zkClient.createPersistent(znodePath);
				}
				znodePath = znodePath + "/" + host;
				zkClient.createEphemeral(znodePath);
			} catch (Exception e) {

			}
		}
		log.info("register service:{} success", className);
	}

	@Override
	public ServiceInstance lookup(String descriptor) {
		ServiceInstance instance = serverCaches.get(descriptor);
		if (instance == null) {
			throw new NoAvailableServiceException(descriptor.substring(0, descriptor.indexOf("\n")));
		}
		return instance;
	}
}
