package com.FoolRpc.rpc.common.util;

/**
 * 通用工具类
 *
 * @author luolinyuan
 * @date 2022/5/17
 **/
public class CommonUtils {
	public static String getInterfaceName(String descriptor) {
		descriptor = descriptor.substring(0, descriptor.indexOf("\n"));
		descriptor = descriptor.replace("\"", "");
		descriptor = descriptor.replace("clazz: ", "");
		return descriptor;
	}
}
