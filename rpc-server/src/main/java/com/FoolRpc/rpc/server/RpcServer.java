package com.FoolRpc.rpc.server;

/**
 * Rpc服务接口
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public interface RpcServer {
	/**
	 * 注册服务
	 *
	 * @param interfaceClass 注册的服务接口
	 * @param bean           接口实现类
	 **/
	public <T> void register(Class<T> interfaceClass, T bean);

	/**
	 * 启动Rpc服务端
	 **/
	public void start();

	/**
	 * 关闭Rpc服务端
	 **/
	public void stop();
}
