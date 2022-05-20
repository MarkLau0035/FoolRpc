package com.foolrpc.rpc.registry.zookeeper;

import com.foolrpc.rpc.common.constants.Constants;
import com.foolrpc.rpc.registry.config.ZookeeperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * Zookeeper 客户端
 *
 * @author luolinyuan
 * @date 2022/4/29
 **/
@Slf4j
@Component
@Scope("prototype")
@ConditionalOnBean(ZookeeperConfig.class)
public class ZkCuratorClient {

	private CuratorFramework client;

	private static final Charset CHARSET = Constants.CHARSET;


	@Autowired
	public ZkCuratorClient(ZookeeperConfig config) {
		RetryPolicy retryPolicy = new RetryNTimes(config.getMaxRetryTimes(), config.getBaseSleepTimeMs());

		StringJoiner joiner = new StringJoiner(",");

		for (String zkPeer : config.getPeers()) {
			joiner.add(zkPeer);
		}
		try {
			client = CuratorFrameworkFactory.builder()
					.connectString(joiner.toString())
					.sessionTimeoutMs(config.getSessionTimeOut())
					.retryPolicy(retryPolicy)
					.namespace(config.getNamespace())
					.connectionTimeoutMs(config.getConnectionTimeout())
					.build();
			client.start();
			boolean connected = client.blockUntilConnected(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
			if (!connected) {
				throw new IllegalStateException("zookeeper try to connect fail");
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void doClose() {
		client.close();
	}


	/**
	 * 递归的创建永久结点
	 *
	 * @param path 路径
	 **/
	public void createPersistent(String path, String data) {
		byte[] dataBytes = data.getBytes(CHARSET);
		try {
			createPersistent(path);
			client.setData().forPath(path, dataBytes);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}


	public void createPersistent(String path) {
		try {
			client.create()
					.creatingParentContainersIfNeeded()
					.withMode(CreateMode.PERSISTENT)
					.forPath(path);
		} catch (KeeperException.NoNodeException ignored) {

		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}

	}

	public void createEphemeral(String path) {
		try {
			client.create()
					.creatingParentContainersIfNeeded()
					.withMode(CreateMode.EPHEMERAL)
					.forPath(path);
		} catch (KeeperException.NodeExistsException e) {
			log.warn("ZNode: " + path + " is Exists try to delete it and create..", e);
			deletePath(path);
			createEphemeral(path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * 递归创建临时结点
	 *
	 * @param path 结点路径
	 **/
	public void createEphemeral(String path, String data) {
		byte[] dataBytes = data.getBytes(CHARSET);
		try {
			client.create()
					.creatingParentContainersIfNeeded()
					.withMode(CreateMode.EPHEMERAL)
					.forPath(path, dataBytes);
		} catch (KeeperException.NodeExistsException e) {
			log.warn("ZNode: " + path + " is Exists try to delete it and create..", e);
			deletePath(path);
			createEphemeral(path, data);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * 递归删除结点
	 *
	 * @param path 删除结点路径
	 **/
	public void deletePath(String path) {
		try {
			client.delete()
					.guaranteed()
					.deletingChildrenIfNeeded()
					.forPath(path);
		} catch (KeeperException.NoNodeException ignored) {
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void create(String path, String data, boolean ephemeral) {
		if (ephemeral) {
			createEphemeral(path, data);
		} else {
			createPersistent(path, data);
		}
	}

	public void update(String path, String data, int version) {
		byte[] dataBytes = data.getBytes(CHARSET);
		try {
			client.setData().withVersion(version).forPath(path, dataBytes);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void createOrUpdatePersistent(String path, String data, int version) {
		try {
			if (checkExists(path)) {
				update(path, data, version);
			} else {
				createPersistent(path, data);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	protected void createOrUpdateEphemeral(String path, String data, int version) {
		try {
			if (checkExists(path)) {
				update(path, data, version);
			} else {
				createEphemeral(path, data);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void update(String path, String data) {
		byte[] dataBytes = data.getBytes(CHARSET);
		try {
			client.setData().forPath(path, dataBytes);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}


	public List<String> getChildren(String path) {
		try {
			return client.getChildren().forPath(path);
		} catch (KeeperException.NoNodeException e) {
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public boolean checkExists(String path) {
		try {
			if (client.checkExists().forPath(path) != null) {
				return true;
			}
		} catch (Exception ignored) {
		}
		return false;
	}

	public String doGetContent(String path) {
		try {
			byte[] dataBytes = client.getData().forPath(path);
			return (dataBytes == null || dataBytes.length == 0) ? null : new String(dataBytes, CHARSET);
		} catch (KeeperException.NoNodeException e) {
			// ignore NoNode Exception.
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return null;
	}

	public byte[] doGetContentOriginal(String path) {
		try {
			return client.getData().forPath(path);
		} catch (KeeperException.NoNodeException e) {
			// ignore NoNode Exception.
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return null;
	}

/*
	服务发现相关 待实现
	public CuratorWatcherImpl createTargetChildListener(String path, ChildListener listener) {
		return new CuratorWatcherImpl(client, listener, path);
	}

	public List<String> addTargetChildListener(String path, CuratorWatcherImpl listener) {
		try {
			return client.getChildren().usingWatcher(listener).forPath(path);
		} catch (KeeperException.NoNodeException e) {
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public NodeCacheListenerImpl createTargetDataListener(String path, DataListener listener) {
		return new NodeCacheListenerImpl(client, listener, path);
	}

	protected void addTargetDataListener(String path, NodeCacheListenerImpl nodeCacheListener) {
		this.addTargetDataListener(path, nodeCacheListener, null);
	}

	protected void addTargetDataListener(String path, NodeCacheListenerImpl nodeCacheListener, Executor executor) {
		try {
			NodeCache nodeCache = new NodeCache(client, path);
			if (nodeCacheListener.nodeCacheMap.putIfAbsent(path, nodeCache) != null) {
				return;
			}
			if (executor == null) {
				nodeCache.getListenable().addListener(nodeCacheListener);
			} else {
				nodeCache.getListenable().addListener(nodeCacheListener, executor);
			}

			nodeCache.start();
		} catch (Exception e) {
			throw new IllegalStateException("Add nodeCache listener for path:" + path, e);
		}
	}

	protected void removeTargetDataListener(String path, NodeCacheListenerImpl nodeCacheListener) {
		NodeCache nodeCache = NodeCacheListenerImpl.nodeCacheMap.get(path);
		if (nodeCache != null) {
			nodeCache.getListenable().removeListener(nodeCacheListener);
		}
		nodeCacheListener.dataListener = null;
	}

	public void removeTargetChildListener(String path, CuratorWatcherImpl listener) {
		listener.unwatch();
	}
*/
}
