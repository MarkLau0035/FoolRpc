package cn.llynsyw.rpc.server.impl;

import cn.llynsyw.rpc.registry.service.ServiceManager;
import cn.llynsyw.rpc.server.RpcServer;
import cn.llynsyw.rpc.server.config.RpcServerConfig;
import cn.llynsyw.rpc.transport.RequestHandler;
import cn.llynsyw.rpc.transport.TransportServer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/3/30
 **/

@Slf4j
@Component
@ComponentScan({"cn.llynsyw.rpc.registry","cn.llynsyw.rpc.transport"})
public class RpcServerImpl implements RpcServer {

	private RpcServerConfig serverConfig;

	private ServiceManager serviceManager;

	private TransportServer serverNetwork;

	private RequestHandler handler;

	@Autowired
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	@Autowired
	public void setServerNetwork(TransportServer serverNetwork) {
		this.serverNetwork = serverNetwork;
	}


	@Autowired
	public void setServerConfig(RpcServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Autowired
	public void setHandler(RequestHandler handler) {
		this.handler = handler;
	}

	public RpcServerImpl() {}

	@Override
	public <T> void register(Class<T> interfaceClass, T bean) {
		serviceManager.register(interfaceClass, bean,serverConfig.getHost(),serverConfig.getPort());
	}


	@Override
	public void start() {
		this.serverNetwork.init(serverConfig.getPort(),handler);
		this.serverNetwork.start();
	}

	@Override
	public void stop() {
		this.serverNetwork.stop();
	}
}
