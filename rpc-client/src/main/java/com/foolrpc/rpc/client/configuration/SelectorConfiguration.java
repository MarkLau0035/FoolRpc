package com.foolrpc.rpc.client.configuration;

import com.foolrpc.rpc.client.impl.RandomTransportSelector;
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

}
