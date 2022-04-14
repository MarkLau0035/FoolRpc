package cn.llynsyw.rpc.server;

import cn.llynsyw.rpc.common.ProtoUtils;
import cn.llynsyw.rpc.common.ReflectionUtils;
import cn.llynsyw.rpc.proto.Request;
import cn.llynsyw.rpc.proto.Response;
import cn.llynsyw.rpc.transport.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/30
 **/

@Slf4j
public class RpcServer {
	private RpcServerConfig config;
	private TransportServer net;
	private ServiceManager serviceManager;
	private ServiceInstance serviceInstance;
	private ServiceInvoker serviceInvoker;

	public RpcServer(RpcServerConfig config) {
		this.config = config;

		this.net = ReflectionUtils.newInstance(config.getTransportClass());
		this.net.init(config.getPort(), this.handler);

		this.serviceManager = new ServiceManager();
		this.serviceInvoker = new ServiceInvoker();
	}

	public RpcServer() {
		this(new RpcServerConfig());
	}


	public <T> void register(Class<T> interfaceClass, T bean) {
		serviceManager.register(interfaceClass, bean);
	}

	public void start() {
		this.net.start();
	}

	public void stop() {
		this.net.stop();
	}

	private RequestHandler handler = new RequestHandler() {
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
	};
}
