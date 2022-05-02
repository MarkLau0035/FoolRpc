package cn.llynsw.rpc.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/
@Data
@Component
@ConfigurationProperties(prefix = "rpc.client")
public class RpcClientConfig {

	/**
	 * 从配置文件读入连接个数 默认为1
	 **/
	private int connectCount = 1;

	private List<String> urls;

}
