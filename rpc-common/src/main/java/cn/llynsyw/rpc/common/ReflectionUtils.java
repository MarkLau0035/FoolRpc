package cn.llynsyw.rpc.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public class ReflectionUtils {
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}


	/**
	 * @return java.lang.reflect.Method[]
	 * @params [java.lang.Class]
	 * @description TODO
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
	 * @return java.lang.Object
	 * @params [java.lang.Object, java.lang.reflect.Method, java.lang.Object...]
	 * @description TODO
	 **/
	public static Object invoke(Object obj, Method method, Object... args) {
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}



}

