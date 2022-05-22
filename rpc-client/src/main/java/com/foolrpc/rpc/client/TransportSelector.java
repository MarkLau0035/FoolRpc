package com.foolrpc.rpc.client;

import com.foolrpc.rpc.registry.zookeeper.NotifyTarget;
import com.foolrpc.rpc.transport.TransportClient;

import java.util.List;

/**
 * 选择哪个server连接
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/
public interface TransportSelector extends NotifyTarget {


	/**
	 * 初始化selector
	 *
	 * @param urls 网络客户端
	 **/
	void init(List<String> urls);

	/**
	 * 选取一个TransportClient
	 *
	 * @return com.foolrpc.rpc.transport.TransportClient
	 **/
	TransportClient select();

	/**
	 * 归还Transport客户端
	 *
	 * @param client 被归还的网络客户端
	 **/
	void release(TransportClient client);


	/**
	 * 关闭所有的网络客户端
	 **/
	void close();
}
