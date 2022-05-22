package com.foolrpc.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/5/22
 **/
public class ProvidersListener implements PathChildrenCacheListener {
	private NotifyTarget target;

	protected ProvidersListener(NotifyTarget target) {
		this.target = target;
	}

	@Override
	public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
		ChildData data = event.getData();
		String path = (data == null) ? null : data.getPath();
		switch (event.getType()) {
			case CHILD_ADDED:
				target.onReceiveRegistryEvent(NotifyTarget.EventType.CHILD_ADDED, path);
				break;
			case CHILD_REMOVED:
				target.onReceiveRegistryEvent(NotifyTarget.EventType.CHILD_DELETED, path);
				break;
			default:
				break;
		}
	}
}
