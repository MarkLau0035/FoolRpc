package cn.llynsyw.server;

import cn.llynsyw.rpc.*;
import com.llynsyw.rpc.Decoder;
import com.llynsyw.rpc.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/30
 **/

@Slf4j
public class RpcServer {
    private RpcServerConfig config;
    private TransportServer net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private ServiceInstance serviceInstance;
    private ServiceInvoker serviceInvoker;

    public RpcServer(RpcServerConfig config) {
        this.config = config;

        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);

        this.encoder = ReflectionUtils.newInstance(config.getEncoder());

        this.decoder = ReflectionUtils.newInstance(config.getDecoder());

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
            Response resp = new Response();
            Request request = null;
            try {
                byte[] inBytes = IOUtils.readFully(receive, receive.available());
                request = decoder.decode(inBytes, Request.class);

                log.info("get request: {}", request);
                ServiceInstance sis = serviceManager.lookup(request);

                Object ret = serviceInvoker.invoke(sis, request);
                resp.setData(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                resp.setCode(1);
                resp.setMessage("RpcServer got error"
                        + e.getClass().getName()
                        + " : " + e.getMessage());
            } finally {
                byte[] outBytes = encoder.encode(resp);
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
