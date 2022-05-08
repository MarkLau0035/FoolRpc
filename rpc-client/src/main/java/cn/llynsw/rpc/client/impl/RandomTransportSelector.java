package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsyw.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
//@Component
public class RandomTransportSelector implements TransportSelector {
	/**
	 * 表示已经连接好的client
	 */
	private List<TransportClient> clients;

	public RandomTransportSelector() {
		clients = new ArrayList<>();
	}

	@Override
	public synchronized void init(List<String> urls, int count, TransportClient clientNetwork) {
		count = Math.max(count, 1);
		for (String url : urls) {
			for (int i = 0; i < count; i++) {
				clientNetwork.connect(url);
				clients.add(clientNetwork);
			}
			log.info("connect server  : {}", url);
		}
	}
	@Override
	public synchronized void init(String url, int count, TransportClient clientNetwork) {
		count = Math.max(count, 1);
		for (int i = 0; i < count; i++) {
			clientNetwork.connect(url);
			clients.add(clientNetwork);
			log.info("connect server  : {}", url);
		}
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
