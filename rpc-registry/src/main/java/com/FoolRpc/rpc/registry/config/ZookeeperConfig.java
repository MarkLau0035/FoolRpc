package com.FoolRpc.rpc.registry.config;

import lombok.Data;

import java.util.List;

import com.FoolRpc.rpc.common.constants.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * zookeeper configuration
 *
 * @author luolinyuan
 * @date 2022/4/29
 **/
@Data
@Component
@ComponentScan("com.FoolRpc.rpc.common")
@ConfigurationProperties(prefix = "rpc.registry.zookeeper")
@ConditionalOnProperty(prefix = "rpc", name = "connect-mod", havingValue = "registry")
public class ZookeeperConfig {

	/**
	 * zookeeper服务结点
	 */
	private List<String> peers;

	private int maxRetryTimes = Constants.ZOOKEEPER_MAX_RETRY_TIMES;

	private int baseSleepTimeMs = Constants.ZOOKEEPER_BASE_SLEEP_TIME_MS;

	private int sessionTimeOut = Constants.ZOOKEEPER_SESSION_TIME_OUT;

	private String namespace = Constants.ZOOKEEPER_NAMESPACE;

	int connectionTimeout = Constants.ZOOKEEPER_CONNECTION_TIMEOUT_MS;
}
