package cn.llynsyw.rpc.transport;

import java.io.InputStream;

/**
 * @Description 1.创建连接 2.发送数据 等待 3.关闭连接
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public interface TransportClient {
	/**
	 * 连接一个结点
	 *
	 * @param peer 具体结点
	 **/
	void connect(Peer peer);

	/**
	 * 写入数据流
	 *
	 * @param data 数据流
	 * @return java.io.InputStream
	 **/
	InputStream write(InputStream data);

	/**
	 * 释放资源
	 **/
	void close();
}
