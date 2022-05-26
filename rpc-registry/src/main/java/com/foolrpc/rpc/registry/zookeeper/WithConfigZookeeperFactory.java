package com.foolrpc.rpc.registry.zookeeper;

import org.apache.curator.utils.ZookeeperFactory;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZKClientConfig;

/**
 * WithConfigZookeeperFactory
 *
 * @author luolinyuan
 * @date 2022/5/23
 **/
public class WithConfigZookeeperFactory implements ZookeeperFactory {

	ZKClientConfig config;

	public WithConfigZookeeperFactory(ZKClientConfig config) {
		this.config = config;
	}

	@Override
	public ZooKeeper newZooKeeper(String connectString, int sessionTimeout, Watcher watcher, boolean canBeReadOnly) throws Exception {
		return new ZooKeeper(connectString, sessionTimeout, watcher, canBeReadOnly, this.config);
	}
}
