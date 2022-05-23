package com.foolrpc.rpc.registry.zookeeper;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/5/22
 **/
public interface NotifyTarget {

	enum EventType {
		/***/
		CHILD_ADDED,
		CHILD_DELETED,
		CHILD_MODIFIED,
		CHILD_LOST
	}

	/**
	 * 接受来自注册中心的事件通知
	 *
	 * @param type 事件类型
	 * @param path 触发事件的结点路径
	 **/
	void onReceiveRegistryEvent(EventType type,String path);
}
