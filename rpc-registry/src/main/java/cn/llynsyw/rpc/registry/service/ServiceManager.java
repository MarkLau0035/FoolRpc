package cn.llynsyw.rpc.registry.service;

import cn.llynsyw.rpc.common.bean.ServiceInstance;
import cn.llynsyw.rpc.protocol.Request;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public interface ServiceManager {
	/**
	 * 注册服务
	 *
	 * @param interfaceClass 注册的服务接口
	 * @param bean           接口实现类
	 **/
	<T> void register(Class<T> interfaceClass, T bean,String host,int port);

	/**
	 * 通过请求查询服务类
	 * @param request 接受的请求
	 * @return cn.llynsyw.rpc.server.bean.ServiceInstance
	  **/
	 ServiceInstance lookup(Request request);

}
