package cn.llynsyw.rpc.common;

import cn.llynsyw.rpc.proto.ServiceDescriptor;

import java.lang.reflect.Method;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/4/13
 **/
public class RegistryUtils {
	public static ServiceDescriptor serviceRegister(Class clazz, Method method) {
		ServiceDescriptor.Builder sdpBuilder = ServiceDescriptor.newBuilder();
		Class[] parameterClasses = method.getParameterTypes();
		for (Class parameterClass : parameterClasses) {
			sdpBuilder.addParameterType(parameterClass.getTypeName());
		}
		return sdpBuilder.setClazz(clazz.getName())
				.setMethodName(method.getName())
				.setReturnType(method.getReturnType().getName())
				.build();
	}
}
