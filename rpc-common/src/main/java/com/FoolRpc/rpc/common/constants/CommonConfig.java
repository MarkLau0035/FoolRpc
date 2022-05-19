package com.FoolRpc.rpc.common.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通用配置
 *
 * @author luolinyuan
 * @date 2022/5/17
 **/
@Component
@ConfigurationProperties(prefix = "rpc")
@Data
public class CommonConfig {
	private ConnectMod connectMod = ConnectMod.DIRECT;


}
