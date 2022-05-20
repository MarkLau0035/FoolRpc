package com.foolrpc.rpc.client.impl;

import com.foolrpc.rpc.FoolRpcClientFactory;
import com.foolrpc.rpc.client.RpcClient;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import stub.Calculator;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoolRpcClientFactory.class)
public class RpcClientImplTest {

	@Autowired
	private RpcClient rpcClient;


	@Test
	@Ignore
	public void TestInvoke() {
		/*测试整型加法的调用*/
		Calculator calculator = rpcClient.getProxy(Calculator.class);
		int result = calculator.add(1, 2);
		assertEquals(3, result);
	}

	@Test
	@Ignore
	public void TestInvoke2() {
		/*测试double类型的调用*/
		Calculator calculator = rpcClient.getProxy(Calculator.class);
		double result = calculator.add(3.22, 1.58);
		assertEquals(4.80, result, 0.01);
	}
}