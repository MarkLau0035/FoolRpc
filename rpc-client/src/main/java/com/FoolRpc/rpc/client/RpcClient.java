package com.FoolRpc.rpc.client;

/**
 * rpc客户端
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public interface RpcClient {
	/**
	 * 获得接口的远程代理对象
	 * @param clazz 服务的类型
	 * @return T    返回服务的代理对象
	  **/
	<T> T getProxy(Class<T> clazz);

}
