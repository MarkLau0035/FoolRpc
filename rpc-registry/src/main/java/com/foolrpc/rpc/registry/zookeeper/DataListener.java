package com.foolrpc.rpc.registry.zookeeper;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/4/30
 **/
public interface DataListener {

	void dataChanged(String path, Object value, EventType eventType);
}
