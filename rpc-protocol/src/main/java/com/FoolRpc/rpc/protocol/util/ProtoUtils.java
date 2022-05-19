package com.FoolRpc.rpc.protocol.util;


import com.FoolRpc.rpc.protocol.Request;
import com.FoolRpc.rpc.protocol.Response;
import com.FoolRpc.rpc.protocol.ServiceDescriptor;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Proto工具类
 *
 * @author luolinyuan
 * @date 2022/4/13
 **/
public class ProtoUtils {
	/**
	 * 从具体参数生成请求
	 *
	 * @param clazz  请求服务接口
	 * @param method 请求的方法
	 * @param args   请求的具体携带参数
	 * @return com.FoolRpc.rpc.proto.Request
	 **/
	public static Request generateRequest(Class clazz, Method method, Object... args) {
		Request.Builder requestBuilder = Request.newBuilder();


		if (args.length != 0) {
			for (Object arg : args) {
				try {
					requestBuilder.addParameter(ByteString.copyFrom(objectToBytes(arg)));
				} catch (IOException e) {
					throw new IllegalStateException("generateRequest From Object is failure");
				}
			}
		}

		requestBuilder.setServiceDescriptor(generateServiceDescriptor(clazz, method));
		return requestBuilder.build();
	}

	/**
	 * 从字节流生成请求对象
	 *
	 * @param in 请求对象字节流
	 * @return com.FoolRpc.rpc.proto.Request
	 **/
	public static Request generateRequest(byte[] in) throws InvalidProtocolBufferException {
		return Request.parseFrom(in);
	}


	/**
	 * 从给定参数生成响应
	 *
	 * @param code    状态码
	 * @param message 信息
	 * @param data    携带数据
	 * @return com.FoolRpc.rpc.proto.Response
	 **/
	public static Response generateResponse(int code, String message, Object... data) {
		try {
			return Response.newBuilder()
					.setCode(Response.Code.forNumber(code))
					.setMessage(message)
					.setData(ByteString.copyFrom(objectToBytes(data)))
					.build();
		} catch (IOException e) {
			throw new IllegalStateException("generateResponse From Object is failure");
		}
	}

	/**
	 * 从字节序列生成响应对象
	 *
	 * @param in 响应字节序列
	 * @return com.FoolRpc.rpc.proto.Response
	 **/
	public static Response generateResponse(byte[] in) throws InvalidProtocolBufferException {
		return Response.parseFrom(in);
	}

	public static Response generateResponse(Object obj) throws InvalidProtocolBufferException {
		try {
			return Response.newBuilder().setData(ByteString.copyFrom(objectToBytes(obj))).build();
		} catch (IOException e) {
			throw new IllegalStateException("generateResponse From Object is failure");
		}
	}

	/**
	 * 对象转字节数组
	 */
	public static byte[] objectToBytes(Object obj) throws IOException {
		try (
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream sOut = new ObjectOutputStream(out);
		) {
			sOut.writeObject(obj);
			sOut.flush();
			byte[] bytes = out.toByteArray();
			return bytes;
		}
	}

	/**
	 * 字节数组转对象
	 */
	public static Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		try (
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				ObjectInputStream sIn = new ObjectInputStream(in);
		) {
			return sIn.readObject();
		}
	}

	public static ServiceDescriptor generateServiceDescriptor(Class clazz, Method method) {
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
