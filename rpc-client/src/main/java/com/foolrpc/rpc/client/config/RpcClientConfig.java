package com.foolrpc.rpc.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Rpc客户端配置
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/
@Data
@Component
@ConfigurationProperties(prefix = "rpc.client")
public class RpcClientConfig {

	/**
	 * the count of connection
	 **/
	private int connectCount = 1;
	/**
	 * the urls for directly connect
	 */
	private List<String> urls;
}
