package cn.llynsw.rpc.client;

import cn.llynsyw.rpc.transport.TransportClient;

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
	 * @param urls  链接提供了该服务的sever端
	 * @param count 表示client与server建立多少个连接
	 * @param clientNetwork client实现类
	 **/
	void init(List<String> urls, int count, TransportClient clientNetwork);
	void init(String urls, int count, TransportClient clientNetwork);

	/**
	 * 选出一个rpc客户端使用
	 *
	 **/
	TransportClient select();

	/**
	 * 归还rpc客户端
	 *
	 * @param client 被归还的客户端
	 **/
	void release(TransportClient client);


	/**
	 * 关闭所有的rpc客户端
	 *
	 **/
	void close();
}
