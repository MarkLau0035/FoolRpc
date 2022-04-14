package cn.llynsyw.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 表示服务
 *
 * @author luolinyuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDescriptor {
	private String clazz;
	private String method;
	private String returnType;
	private String[] parameterType;

	public static ServiceDescriptor from(Class clazz, Method method) {
		ServiceDescriptor sdp = new ServiceDescriptor();
		sdp.setClazz(clazz.getName());
		sdp.setMethod(method.getName());
		sdp.setReturnType(method.getReturnType().getTypeName());

		Class[] parameterClasses = method.getParameterTypes();
		String[] parameterTypes = new String[parameterClasses.length];
		for (int i = 0; i < parameterClasses.length; i++) {
			parameterTypes[i] = parameterClasses[i].getName();
		}
		return sdp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ServiceDescriptor that = (ServiceDescriptor) o;
		return Objects.equals(clazz, that.clazz) && Objects.equals(method, that.method) && Objects.equals(returnType, that.returnType) && Arrays.equals(parameterType, that.parameterType);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return "ServiceDescriptor{" + "clazz='" + clazz + '\'' + ", method='" + method + '\'' + ", returnType='" + returnType + '\'' + ", parameterType=" + Arrays.toString(parameterType) + '}';
	}
}
