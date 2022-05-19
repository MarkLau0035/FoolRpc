package com.FoolRpc.rpc.server.configuration;

import com.FoolRpc.rpc.transport.TransportServer;
import com.FoolRpc.rpc.transport.impl.HttpTransportSever;
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
		return new HttpTransportSever();
	}
}
