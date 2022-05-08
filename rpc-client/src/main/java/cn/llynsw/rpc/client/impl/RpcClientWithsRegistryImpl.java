package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.RpcClient;
import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsw.rpc.client.config.RpcClientConfig;
import cn.llynsw.rpc.client.configuration.SelectConfiguration;
import cn.llynsyw.rpc.registry.zookeeper.ZkCuratorClient;
import cn.llynsyw.rpc.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/5/1
 **/
@Component
@ComponentScan("cn.llynsyw.rpc.registry")
@AutoConfigureAfter(ZkCuratorClient.class)
@ConditionalOnProperty(prefix = "rpc", name = "connectMod", havingValue = "registry")
public class RpcClientWithsRegistryImpl implements RpcClient {
	private TransportSelector selector;

	private RpcClientConfig config;

	private ZkCuratorClient zkClient;

	private TransportClient clientNetwork;

	@Autowired
	private ApplicationContext context;

	@Autowired
	public RpcClientWithsRegistryImpl(RpcClientConfig config, ZkCuratorClient zkClient) {
		this.config = config;
		this.zkClient = zkClient;

	}

	@Override
	public <T> T getProxy(Class<T> clazz) {
		getProviders(clazz);
		return (T) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[]{clazz},
				new RemoteInvoker(clazz, selector)
		);
	}

	@Override
	public void setSelector(TransportSelector selector) {
		this.selector = selector;
	}

	public void getProviders(Class clazz) {
		List<String> children = zkClient.getChildren("/" + clazz.getName() + "/provider");
		if (children != null && !children.isEmpty()) {
			//selector = new RandomTransportSelector();
			selector = context.getBean(TransportSelector.class);
			for (String url:
				 children) {
				clientNetwork = context.getBean(TransportClient.class);
				selector.init(url, config.getConnectCount(), clientNetwork);
			}

			//setSelector(selector);
		} else {
			throw new IllegalStateException("there is not exist such service");
		}
	}
}
