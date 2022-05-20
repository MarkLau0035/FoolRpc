package com.foolrpc.rpc.client.impl;

import com.foolrpc.rpc.client.TransportSelector;
import com.foolrpc.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private List<TransportClient> clients;

	public RandomTransportSelector() {
		clients = new ArrayList<>();
	}


	@Override
	public void init(List<TransportClient> clients) {
		this.clients = clients;
	}

	@Override
	public synchronized TransportClient select() {
		int i = new Random().nextInt(clients.size());
		return clients.remove(i);
	}

	@Override
	public synchronized void release(TransportClient client) {
		clients.add(client);
	}

	@Override
	public synchronized void close() {
		for (TransportClient client : clients) {
			client.close();
		}
		clients.clear();
	}
}
