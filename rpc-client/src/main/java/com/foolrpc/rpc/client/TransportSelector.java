package com.foolrpc.rpc.client;

import com.foolrpc.rpc.transport.TransportClient;

import java.util.List;

/**
 * 选择哪个server连接
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/
public interface TransportSelector {


	/**
	 * 初始化selector
	 *
	 * @param clients 网络客户端
	  **/
	void init(List<TransportClient> clients);

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
