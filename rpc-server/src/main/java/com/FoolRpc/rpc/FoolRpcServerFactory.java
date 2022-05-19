package com.FoolRpc.rpc;

import com.FoolRpc.rpc.server.RpcServer;
import com.FoolRpc.rpc.server.impl.RpcServerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * FoolRpcServerFactory
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
@SpringBootApplication
public class FoolRpcServerFactory {
	public static RpcServer createServer(String... args) {
		ConfigurableApplicationContext run = SpringApplication.run(FoolRpcServerFactory.class, args);
		return run.getBean(RpcServerImpl.class);
	}

}
