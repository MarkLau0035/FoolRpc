package com.FoolRpc.rpc.client.configuration;

import com.FoolRpc.rpc.client.impl.RandomTransportSelector;
import com.FoolRpc.rpc.transport.TransportClient;
import com.FoolRpc.rpc.transport.impl.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author lgc
 */
@Configuration(proxyBeanMethods = false)
public class SelectorConfiguration {


	@Bean
	@Scope("prototype")
	public RandomTransportSelector nodeSelector() {
		return new RandomTransportSelector();
	}

	@Bean
	@Scope("prototype")
	public TransportClient netClient() {
		return new HttpTransportClient();
	}
}
