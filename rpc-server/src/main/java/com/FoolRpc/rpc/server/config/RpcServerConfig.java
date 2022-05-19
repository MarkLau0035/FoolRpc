package com.FoolRpc.rpc.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * configuration of RpcServer
 *
 * @author luolinyuan
 * @date 2022/3/29
 **/
@Data
@Component
@ConfigurationProperties(prefix = "rpc.server")
public class RpcServerConfig {

	/**
	 * server ip
	 **/
	private String ip = "127.0.0.1";

	/**
	 * server port
	 **/
	private int port = 8081;
}
