package cn.llynsyw.rpc.registry.config;

import lombok.Data;

import java.util.List;

import cn.llynsyw.rpc.common.constants.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zookeeper configuration
 *
 * @author luolinyuan
 * @date 2022/4/29
 **/
@Data
@Component
@ConfigurationProperties(prefix = "rpc.registry.zookeeper")
@ConditionalOnProperty(prefix = "rpc",name = "connectMod",havingValue = "registry")
public class ZookeeperConfig {

	//zookeeper服务结点
	private List<String> peers;

	//定义失败重试次数
	private int maxRetryTimes = Constants.ZOOKEEPER_MAX_RETRY_TIMES;

	//会话存活时间 单位:毫秒
	private int baseSleepTimeMs = Constants.ZOOKEEPER_BASE_SLEEP_TIME_MS;

	//连接失败后，再次重试的间隔时间 单位:毫秒
	private int sessionTimeOut = Constants.ZOOKEEPER_SESSION_TIME_OUT;

	private String namespace = Constants.ZOOKEEPER_NAMESPACE;

	int connectionTimeout = Constants.ZOOKEEPER_CONNECTION_TIMEOUT_MS;
}
