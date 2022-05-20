package com.foolrpc.rpc.common.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 常量
 *
 * @author luolinyuan
 * @date 2022/4/29
 **/
public interface Constants {


	String ZOOKEEPER_NAMESPACE = "FoolRpc";

	int ZOOKEEPER_MAX_RETRY_TIMES = 3;

	int ZOOKEEPER_BASE_SLEEP_TIME_MS = 5000;

	int ZOOKEEPER_SESSION_TIME_OUT = 60 * 1000;

	int ZOOKEEPER_CONNECTION_TIMEOUT_MS = 3 * 1000;

	Charset CHARSET = StandardCharsets.UTF_8;

	int NETWORK_CONNECTION_TIMEOUT_MS = 3 * 1000;

	int NETWORK_SESSION_TIME_TIMEOUT_MS = 30 * 1000;

}
