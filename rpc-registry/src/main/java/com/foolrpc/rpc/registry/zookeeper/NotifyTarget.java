package com.foolrpc.rpc.registry.zookeeper;

import org.apache.curator.framework.recipes.cache.ChildData;

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
	 * TODO
	 *
	 * @param type 时间类型
	 * @param path 触发事件的结点对象
	 **/
	void onReceiveRegistryEvent(EventType type,String path);
}
