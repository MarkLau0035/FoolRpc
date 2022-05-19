package cn.llynsyw.rpc.registry.zookeeper;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/4/30
 **/
public enum EventType {
	None(-1),
	NodeCreated(1),
	NodeDeleted(2),
	NodeDataChanged(3),
	NodeChildrenChanged(4),
	CONNECTION_SUSPENDED(11),
	CONNECTION_RECONNECTED(12),
	CONNECTION_LOST(12),
	INITIALIZED(10);


	private final int intValue;     // Integer representation of value
	// for sending over wire

	EventType(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return intValue;
	}
}
