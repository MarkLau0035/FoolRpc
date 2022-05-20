package com.foolrpc.rpc.server.test;

import com.foolrpc.rpc.FoolRpcServerFactory;
import com.foolrpc.rpc.server.RpcServer;
import org.junit.Before;
import org.junit.Test;
import stub.Calculator;
import stub.impl.MyCalculator;

/**
 * test for RpcServer
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public class RpcServerTest {
	private RpcServer server;


	@Before
	public void init() {
		this.server = FoolRpcServerFactory.createServer();
	}

	@Test
	public void testRegister() {
		server.register(Calculator.class, new MyCalculator());
	}

}
