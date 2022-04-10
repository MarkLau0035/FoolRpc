package cn.llynsyw.rpc.impl;

import cn.llynsyw.rpc.RequestHandler;
import cn.llynsyw.rpc.TransportServer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
@Slf4j
public class HttpTransportSever implements TransportServer {
    private RequestHandler handler;
    private Server server;
    @Override
    public void start() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public void init(int port, RequestHandler handler) {
        this.handler = handler;
        this.server = new Server(port);

        ServletContextHandler ctx = new ServletContextHandler();
        server.setHandler(ctx);

        ServletHolder holder = new ServletHolder(new RequestServlet());
        ctx.addServlet(holder,"/*");

    }

    @Override
    public void stop() {
        if(server != null ){
            try {
                server.stop();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    class RequestServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ServletInputStream inputStream = req.getInputStream();
            OutputStream outputStream = resp.getOutputStream();

            if(handler != null){
                handler.onRequestSteam(inputStream,outputStream);
            }
        }
    }
}
