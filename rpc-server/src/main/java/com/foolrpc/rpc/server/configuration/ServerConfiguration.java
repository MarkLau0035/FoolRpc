package com.foolrpc.rpc.server.configuration;

import com.foolrpc.rpc.transport.TransportServer;
import com.foolrpc.rpc.transport.server.NettyTransportServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ServerConfiguration
 *
 * @author luolinyuan
 * @date 2022/5/17
 **/
@Configuration
public class ServerConfiguration {
	@Bean
	public TransportServer transportServer() {
		return new NettyTransportServer();
	}
}
