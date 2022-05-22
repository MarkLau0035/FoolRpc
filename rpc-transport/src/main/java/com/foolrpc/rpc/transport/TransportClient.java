package com.foolrpc.rpc.transport;


import java.io.InputStream;

/**
 * 1.创建连接 2.发送数据 等待 3.关闭连接
 *
 * @author luolinyuan
 * @date 2022/3/29
 **/
public interface TransportClient {
	/**
	 * 建立网络连接
	 **/
	void connect();

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

	/**
	 * 返回连接状态
	 *
	 * @return boolean 连接状态
	 **/
	boolean isConnected();

	/**
	 * 设置url
	 *
	 * @param url
	 **/
	void setUrl(String url);

	/**
	 * 返回对应的url
	 *
	 * @return java.lang.String
	 **/
	String getUrl();
}
