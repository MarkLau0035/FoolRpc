package com.FoolRpc.rpc.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


/**
 * @author luolinyuan
 */
public class ReflectionUtils {
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 获取该类的所有公共方法
	 *
	 * @param clazz 具体类
	 * @return java.lang.reflect.Method[]
	 **/
	public static Method[] getPublicMethods(Class clazz) {
		/**/
		Method[] methods = clazz.getDeclaredMethods();
		List<Method> methodList = new ArrayList<>();
		/*筛选出public方法*/
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers())) {
				methodList.add(method);
			}
		}
		return methodList.toArray(new Method[0]);
	}


	/**
	 * 执行
	 *
	 * @param obj    调用的对象
	 * @param method 调用的方法
	 * @param args   传入的实参
	 * @return java.lang.Object
	 **/
	public static Object invoke(Object obj, Method method, Object... args) {
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}

