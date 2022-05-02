package cn.llynsw.rpc.client.configuration;

import cn.llynsyw.rpc.transport.TransportClient;
import cn.llynsyw.rpc.transport.impl.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/5/1
 **/
@Configuration
public class BeanConfiguration {

	@Bean
	TransportClient clientNetWorker() {
		return new HttpTransportClient();
	}
}
