package com.FoolRpc.rpc;

import com.FoolRpc.rpc.client.RpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * FoolRpcClientFactory
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
@SpringBootApplication
public class FoolRpcClientFactory {
	public static RpcClient creatClient(String... args){
		ConfigurableApplicationContext run = SpringApplication.run(FoolRpcClientFactory.class,args);
		return run.getBean(RpcClient.class);
	}
}
