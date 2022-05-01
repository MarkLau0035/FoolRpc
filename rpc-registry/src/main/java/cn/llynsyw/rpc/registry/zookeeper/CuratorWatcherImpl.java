package cn.llynsyw.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/4/30
 **/
public class CuratorWatcherImpl implements CuratorWatcher {

	private CuratorFramework client;
	private volatile ChildListener childListener;
	private String path;

	public CuratorWatcherImpl(CuratorFramework client, ChildListener listener, String path) {
		this.client = client;
		this.childListener = listener;
		this.path = path;
	}

	protected CuratorWatcherImpl() {
	}

	public void unwatch() {
		this.childListener = null;
	}

	@Override
	public void process(WatchedEvent event) throws Exception {
		// if client connect or disconnect to server, zookeeper will queue
		// watched event(Watcher.Event.EventType.None, .., path = null).
		if (event.getType() == Watcher.Event.EventType.None) {
			return;
		}

		if (childListener != null) {
			childListener.childChanged(path, client.getChildren().usingWatcher(this).forPath(path));
		}
	}
}
