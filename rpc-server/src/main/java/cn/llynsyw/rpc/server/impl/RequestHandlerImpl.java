package cn.llynsyw.rpc.server.impl;

import cn.llynsyw.rpc.protocol.util.ProtoUtils;
import cn.llynsyw.rpc.protocol.Request;
import cn.llynsyw.rpc.protocol.Response;
import cn.llynsyw.rpc.common.bean.ServiceInstance;
import cn.llynsyw.rpc.registry.service.ServiceManager;
import cn.llynsyw.rpc.transport.RequestHandler;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 请求处理实现类
 * @author luolinyuan
 * @date 2022/4/28
 **/
@Slf4j
@Component
public class RequestHandlerImpl implements RequestHandler {
	private ServiceManager serviceManager;

	private ServiceInvoker serviceInvoker;

	@Autowired
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	@Autowired
	public void setServiceInvoker(ServiceInvoker serviceInvoker) {
		this.serviceInvoker = serviceInvoker;
	}

	@Override
	public void onRequestSteam(InputStream receive, OutputStream toResponse) {

		Response resp = null;
		Request request = null;
		try {
			byte[] inBytes = IOUtils.readFully(receive, receive.available());
			request = ProtoUtils.generateRequest(inBytes);

			log.info("get request: {}", request);
			ServiceInstance sis = serviceManager.lookup(request);
			List<ByteString> parameterList = request.getParameterList();
			Object[] parameters = new Object[parameterList.size()];
			for (int i = 0; i < parameterList.size(); i++) {
				parameters[i] = ProtoUtils.bytesToObject(parameterList.get(i).toByteArray());
			}
			Object ret = serviceInvoker.invoke(sis, parameters);
			resp = ProtoUtils.generateResponse(ret);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			String message = ("RpcServer got error"
					+ e.getClass().getName()
					+ " : " + e.getMessage());
			resp = ProtoUtils.generateResponse(1, message);
		} finally {
			byte[] outBytes = resp.toByteArray();
			try {
				toResponse.write(outBytes);
				log.info("response client");
			} catch (IOException e) {
				log.warn(e.getMessage(), e);
			}
		}
	}
}
