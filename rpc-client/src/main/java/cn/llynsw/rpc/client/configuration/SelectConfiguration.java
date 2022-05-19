package cn.llynsw.rpc.client.configuration;

import cn.llynsw.rpc.client.impl.RandomTransportSelector;
import cn.llynsyw.rpc.transport.TransportClient;
import cn.llynsyw.rpc.transport.impl.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author lgc
 */
@Configuration(proxyBeanMethods = false)
public class SelectConfiguration {
    @Bean
    @Scope("prototype")
   public RandomTransportSelector nodeSelector(){
        return new RandomTransportSelector();
    }
    @Bean
    @Scope("prototype")
   public TransportClient clientNetworkS(){
        return new HttpTransportClient();
    }
}
