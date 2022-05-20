package com.foolrpc.rpc.transport.impl;

import com.foolrpc.rpc.transport.RequestHandler;
import com.foolrpc.rpc.transport.TransportServer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * HttpTransportSever
 *
 * @author luolinyuan
 * @date 2022/3/29
 **/
@Slf4j
public class HttpTransportSever implements TransportServer {
	private RequestHandler handler;
	private Server server;

	@Override
	public void start() {
		/*新增线程启动网络监听*/
		new Thread(() -> {
			try {
				server.start();
				server.join();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}).start();
	}

	@Override
	public void init(int port, RequestHandler handler) {
		this.handler = handler;
		this.server = new Server(port);

		ServletContextHandler ctx = new ServletContextHandler();
		server.setHandler(ctx);

		ServletHolder holder = new ServletHolder(new RequestServlet());
		ctx.addServlet(holder, "/*");
	}

	@Override
	public void stop() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	class RequestServlet extends HttpServlet {
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			ServletInputStream inputStream = req.getInputStream();
			OutputStream outputStream = resp.getOutputStream();

			if (handler != null) {
				handler.onRequestSteam(inputStream, outputStream);
			}
		}
	}
}
