package cn.llynsyw.rpc.registry.zookeeper;

import cn.llynsyw.rpc.common.constants.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author
 * @date 2022/4/30
 **/
public class NodeCacheListenerImpl implements NodeCacheListener {
	private static final Charset CHARSET = Constants.CHARSET;

	private CuratorFramework client;
	protected static Map<String, NodeCache> nodeCacheMap = new ConcurrentHashMap<>();

	protected volatile DataListener dataListener;

	private String path;

	protected NodeCacheListenerImpl() {
	}

	public NodeCacheListenerImpl(CuratorFramework client, DataListener dataListener, String path) {
		this.client = client;
		this.dataListener = dataListener;
		this.path = path;
	}

	@Override
	public void nodeChanged() throws Exception {
		ChildData childData = nodeCacheMap.get(path).getCurrentData();
		String content = null;
		EventType eventType;
		if (childData == null) {
			eventType = EventType.NodeDeleted;
		} else if (childData.getStat().getVersion() == 0) {
			content = new String(childData.getData(), CHARSET);
			eventType = EventType.NodeCreated;
		} else {
			content = new String(childData.getData(), CHARSET);
			eventType = EventType.NodeDataChanged;
		}
		dataListener.dataChanged(path, content, eventType);
	}
}
