package com.FoolRpc.rpc.registry.service;

import com.FoolRpc.rpc.common.bean.ServiceInstance;

import java.util.Map;

/**
 * 服务管理中心
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public interface ServiceManager {
	/**
	 * 服务注册
	 *
	 * @param serviceMap 服务map
	 * @param host       主机
	 **/
	<T> void register(Map<String, ServiceInstance> serviceMap, String className, String host);

	/**
	 * 通过描述符查询服务实例
	 *
	 * @param descriptor 描述符
	 * @return com.FoolRpc.rpc.server.bean.ServiceInstance
	 **/
	ServiceInstance lookup(String descriptor);

}
