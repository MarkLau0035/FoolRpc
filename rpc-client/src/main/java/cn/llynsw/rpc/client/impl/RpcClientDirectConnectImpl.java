package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.RpcClient;
import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsw.rpc.client.config.RpcClientConfig;
import cn.llynsyw.rpc.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/
@Component
@ConditionalOnProperty(prefix = "rpc",name = "connectMod",havingValue = "direct")
public class RpcClientDirectConnectImpl implements RpcClient {

	private TransportSelector selector;

	private RpcClientConfig config;

	private TransportClient clientNetwork;

	@Autowired
	public RpcClientDirectConnectImpl(RpcClientConfig config, TransportClient clientNetwork, TransportSelector selector) {
		this.selector = selector;
		this.config = config;
		this.clientNetwork = clientNetwork;
		if(config.getUrls() == null) {
			throw new IllegalStateException("the urls to connect server is not been specified");
		}
		selector.init(config.getUrls(), config.getConnectCount(), clientNetwork);
	}


	@Override
	public <T> T getProxy(Class<T> clazz) {
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
}
