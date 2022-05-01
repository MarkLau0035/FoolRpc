package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsyw.rpc.protocol.util.ProtoUtils;
import cn.llynsyw.rpc.protocol.Request;
import cn.llynsyw.rpc.protocol.Response;
import cn.llynsyw.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用远程服务的代理类
 * @author luolinyuan
 * @date 2022/3/30
 **/
@Slf4j
public class RemoteInvoker implements InvocationHandler {
	private TransportSelector selector;
	private Class clazz;

	RemoteInvoker(Class clazz,TransportSelector selector) {
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
	public Object invoke(Object proxy, Method method, Object[] args) throws IOException, ClassNotFoundException {
		/*生成Request*/
		Request request = ProtoUtils.generateRequest(this.clazz, method, args);
		/*执行并接收响应*/
		Response resp = invokeRemote(request);
		if (resp == null || resp.getCodeValue() != Response.Code.RPC_OK_VALUE) {
			throw new IllegalStateException("fail to invoke remote:" + resp);
		}
		return ProtoUtils.bytesToObject(resp.getData().toByteArray());
	}


	private Response invokeRemote(Request request) {
		TransportClient client = null;
		Response resp = null;
		try {
			/*选择一个结点*/
			client = selector.select();
			/*转为字节序列并发送*/
			byte[] outBytes = request.toByteArray();
			InputStream receive = client.write(new ByteArrayInputStream(outBytes));
			/*读取响应序列并生成Response对象*/
			byte[] inBytes = IOUtils.readFully(receive, receive.available());
			resp = ProtoUtils.generateResponse(inBytes);
		} catch (IOException e) {
			/*记录并转发异常*/
			log.warn(e.getMessage(), e);
			String message = "RpcClient got error" + e.getClass() + " : " + e.getMessage();
			resp = ProtoUtils.generateResponse(1, message);
		} finally {
			if (client != null) {
				selector.release(client);
			}
		}
		return resp;
	}


}
