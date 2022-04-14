package cn.llynsyw.rpc.server;

import org.junit.Before;
import org.junit.Test;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/4/14
 **/
public class ServerTest {
	RpcServer rpcServer;

	@Before
	public void init() {
		rpcServer = new RpcServer();
	}

	@Test
	public void testStart() {
		//rpcServer.start();
	}

	@Test
	public void testRegister() {
		rpcServer.register(TestInterface.class,new TestInterfaceImpl());
	}
}
