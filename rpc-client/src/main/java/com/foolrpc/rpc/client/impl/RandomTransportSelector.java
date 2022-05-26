package com.foolrpc.rpc.client.impl;

import com.foolrpc.rpc.client.TransportSelector;
import com.foolrpc.rpc.common.exception.NoAvailableServiceException;
import com.foolrpc.rpc.transport.TransportClient;
import com.foolrpc.rpc.transport.client.HttpTransportClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 随机选择rpc服务结点作为rpc客户端
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/
@Slf4j
public class RandomTransportSelector implements TransportSelector {
	/**
	 * 表示已经连接好的client
	 */
	private Map<String, TransportClient> clients;

	public RandomTransportSelector() {
		clients = new ConcurrentHashMap<>();
	}


	@Override
	public void init(List<String> urls) {
		for (String url : urls) {
			TransportClient client = new HttpTransportClient();
			client.setUrl(url);
			clients.put(url, client);
		}
	}

	@Override
	public synchronized TransportClient select() {
		int i = new Random().nextInt(clients.size());
		return clients.remove(clients.keySet().toArray(new String[0])[i]);
	}

	@Override
	public synchronized void release(TransportClient client) {
		clients.put(client.getUrl(), client);
	}

	@Override
	public synchronized void close() {
		for (TransportClient client : clients.values()) {
			client.close();
		}
		clients.clear();
	}

	@Override
	public void onReceiveRegistryEvent(EventType type, String path) {

		/*
			当有新的结点改变的时候触发
			结点路径示例：/cn.llynsyw.example.Calculator/provider/127.0.0.1:10000
		 */

		String[] info = path.split("/");
		String serviceName = info[1];
		String url = info[3];

		switch (type) {
			case CHILD_ADDED:
				if (!clients.containsKey(url)) {
					TransportClient client = new HttpTransportClient();
					client.setUrl(url);
					clients.put(url, client);
					log.info("there's is a new server:{} at {} has been published", serviceName, url);
				}
				break;
			case CHILD_DELETED:
				if (clients.remove(url) != null) {
					log.info("the server:{} at {}  has been offline", serviceName, url);
					if (clients.size() == 0) {
						throw new NoAvailableServiceException(serviceName);
					}
				}
				break;
			default:
				break;
		}
	}
}
