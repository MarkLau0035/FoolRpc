package cn.llynsyw.rpc.server;

import cn.llynsyw.rpc.common.ProtoUtils;
import cn.llynsyw.rpc.common.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/4/14
 **/
public class ServiceInvokerTest {
	ServiceInvoker serviceInvoker;

	@Before
	public void init() {
		serviceInvoker = new ServiceInvoker();
	}

	@Test
	public void testInvoke() throws IOException, ClassNotFoundException {
		Method method = ReflectionUtils.getPublicMethods(TestInterface.class)[0];
		ServiceInstance sis = new ServiceInstance(new TestInterfaceImpl(), method);
		byte[] bytes = ProtoUtils.objectToBytes(1);
		Object o = ProtoUtils.bytesToObject(bytes);
		Object invoke = serviceInvoker.invoke(sis, o);
		System.out.println(invoke);
	}
}
