package cn.llynsw.rpc;

import cn.llynsw.rpc.client.RpcClient;
import cn.llynsw.rpc.client.config.RpcClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * TODO
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
