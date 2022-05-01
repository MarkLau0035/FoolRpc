package cn.llynsyw.rpc.transport;


import java.io.InputStream;

/**
 * 1.创建连接 2.发送数据 等待 3.关闭连接
 * @author luolinyuan
 * @date 2022/3/29
 **/
public interface TransportClient {
	/**
	 * 连接一个结点
	 *
	 * @param url 链接服务的url
	 **/
	void connect(String url);

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
