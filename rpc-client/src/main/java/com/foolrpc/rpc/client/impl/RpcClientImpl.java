package com.foolrpc.rpc.client.impl;

import com.foolrpc.rpc.client.RpcClient;
import com.foolrpc.rpc.client.TransportSelector;
import com.foolrpc.rpc.client.config.RpcClientConfig;
import com.foolrpc.rpc.common.constants.CommonConfig;
import com.foolrpc.rpc.common.constants.ConnectMod;
import com.foolrpc.rpc.common.exception.DoNotExistServerException;
import com.foolrpc.rpc.registry.zookeeper.ZkCuratorClient;
import com.foolrpc.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RpcClientImpl
 *
 * @author luolinyuan
 * @date 2022/5/17
 **/
@Component
@Slf4j
@ComponentScan({"com.foolrpc.rpc.registry"})
public class RpcClientImpl implements RpcClient {

	Map<Class, Object> proxyCaches;

	private ZkCuratorClient zkClient;

	private CommonConfig commonConfig;

	private RpcClientConfig clientConfig;

	RpcClientImpl() {
		proxyCaches = new ConcurrentHashMap<>();
	}

	@Autowired
	private ApplicationContext context;

	@Autowired
	public void setCommonConfig(CommonConfig commonConfig) {
		this.commonConfig = commonConfig;
	}

	@Autowired
	public void setClientConfig(RpcClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	@Autowired(required = false)
	public void setZkClient(ZkCuratorClient zkClient) {
		this.zkClient = zkClient;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clazz) {
		if (proxyCaches.containsKey(clazz)) {
			return (T) proxyCaches.get(clazz);
		}
		TransportSelector selector = getProviders(clazz.getName());
		T proxyInstance = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz},
				new RemoteInvoker(clazz,selector));
		proxyCaches.put(clazz, proxyInstance);
		return proxyInstance;
	}

	TransportSelector getProviders(String clazzName) {
		TransportSelector selector = context.getBean(TransportSelector.class);
		List<String> urlList;
		urlList = (commonConfig.getConnectMod() == ConnectMod.DIRECT) ? getUrlListFromConfig() :
				getUrlListFromRegistry(clazzName);
		List<TransportClient> clientList = new ArrayList<>();
		for (String url : urlList) {
			TransportClient netClient = context.getBean(TransportClient.class);
			netClient.setUrl(url);
			clientList.add(netClient);
		}
		selector.init(clientList);
		return selector;
	}

	List<String> getUrlListFromConfig() {
		List<String> urls = clientConfig.getUrls();
		if (urls != null) {
			return urls;
		} else {
			throw new IllegalStateException("the urls to connect server is not been specified");
		}
	}

	List<String> getUrlListFromRegistry(String clazzName) {
		String znodePath = "/" + clazzName + "/provider";
		List<String> children;
		if (this.zkClient != null && !(children = zkClient.getChildren(znodePath)).isEmpty()) {
			return children;
		} else {
			throw new DoNotExistServerException("Zookeeper Client's initialize failure " +
					"or there is not such service exist");
		}
	}
}
