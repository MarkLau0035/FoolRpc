package cn.llynsyw.rpc.server.test;

import cn.llynsyw.rpc.FoolRpcServerFactory;
import cn.llynsyw.rpc.server.RpcServer;
import org.junit.Before;
import org.junit.Test;
import stub.Calculator;
import stub.impl.MyCalculator;

/**
 * TODO
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
		server.start();
		server.register(Calculator.class, new MyCalculator());
		//server.stop();
	}

}
