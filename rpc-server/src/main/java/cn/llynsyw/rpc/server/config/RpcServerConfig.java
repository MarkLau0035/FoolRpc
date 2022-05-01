package cn.llynsyw.rpc.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/3/29
 **/
@Data
@Component
public class RpcServerConfig {

	/**
	  * 服务发布的端口 默认8081
	    **/
	@Value(value = "${rpc.server.port:8081}")
	private int port;

	/**
	 * 服务的域名 默认为localhost
	  **/
	@Value(value ="${rpc.server.host:localhost}")
	private String host;
}
