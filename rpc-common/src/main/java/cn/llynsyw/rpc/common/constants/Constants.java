package cn.llynsyw.rpc.common.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/4/29
 **/
public interface Constants {

	/*获取服务的方式*/

	//直连
	String DIRECT_CONNECT = "direct";

	//通过zookeeper注册中心
	String THOUGHT_ZOOKEEPER = "zookeeper";

	/*ZOOKEEPER客户端参数默认值*/
	String ZOOKEEPER_NAMESPACE = "FoolRpc";

	//定义失败重试次数
	int ZOOKEEPER_MAX_RETRY_TIMES = 3;

	//会话存活时间 单位:毫秒
	int ZOOKEEPER_BASE_SLEEP_TIME_MS = 5000;

	//连接失败后，再次重试的间隔时间 单位:毫秒
	int ZOOKEEPER_SESSION_TIME_OUT = 60 * 1000;

	//连接超时时间
	int ZOOKEEPER_CONNECTION_TIMEOUT_MS = 30 * 1000;

	Charset CHARSET = StandardCharsets.UTF_8;
}
