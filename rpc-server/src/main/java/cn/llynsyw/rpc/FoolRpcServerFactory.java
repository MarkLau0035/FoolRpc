package cn.llynsyw.rpc;

import cn.llynsyw.rpc.server.RpcServer;
import cn.llynsyw.rpc.server.impl.RpcServerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * TODO
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
