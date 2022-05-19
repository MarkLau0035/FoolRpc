package com.FoolRpc.rpc.client.impl;

import com.FoolRpc.rpc.client.TransportSelector;
import com.FoolRpc.rpc.common.exception.RemoteInvokeFailException;
import com.FoolRpc.rpc.protocol.util.ProtoUtils;
import com.FoolRpc.rpc.protocol.Request;
import com.FoolRpc.rpc.protocol.Response;
import com.FoolRpc.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用远程服务的代理类
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/
@Slf4j
public class RemoteInvoker implements InvocationHandler {
	private TransportSelector selector;
	private Class clazz;

	RemoteInvoker(Class clazz, TransportSelector selector) {
		this.clazz = clazz;
		this.selector = selector;
	}

	/**
	 * 执行
	 *
	 * @param proxy  代理对象
	 * @param method 具体方法
	 * @param args   具体参数
	 * @return java.lang.Object
	 **/
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		/*生成Request*/
		Request request = ProtoUtils.generateRequest(this.clazz, method, args);
		/*执行并接收响应*/
		Response resp = invokeRemote(request);
		if (resp.getCodeValue() != Response.Code.RPC_OK_VALUE) {
			throw new RemoteInvokeFailException(method.getName(), resp.getMessage());
		}
		try {
			return ProtoUtils.bytesToObject(resp.getData().toByteArray());
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}


	private Response invokeRemote(Request request) {
		TransportClient client = null;
		Response resp;
		InputStream receive;
		try {
			/*选择一个结点*/
			client = selector.select();
			if (!client.isConnected()) {
				client.connect();
			}
			/*转为字节序列并发送*/
			byte[] outBytes = request.toByteArray();
			receive = client.write(new ByteArrayInputStream(outBytes));
			/*读取响应序列并生成Response对象*/
			byte[] inBytes = IOUtils.readFully(receive, receive.available());
			resp = ProtoUtils.generateResponse(inBytes);
		} catch (IOException e) {
			String message = "RpcClient got error" + e.getClass() + " : " + e.getMessage();
			resp = ProtoUtils.generateResponse(Response.Code.RPC_INVALID_RESPONSE_VALUE, message);
		} finally {
			if (client != null) {
				selector.release(client);
			}
		}
		return resp;
	}


}
