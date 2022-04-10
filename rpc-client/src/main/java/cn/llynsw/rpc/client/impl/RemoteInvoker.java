package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsyw.rpc.Request;
import cn.llynsyw.rpc.Response;
import cn.llynsyw.rpc.ServiceDescriptor;
import cn.llynsyw.rpc.TransportClient;
import com.llynsyw.rpc.Decoder;
import com.llynsyw.rpc.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description 调用远程服务的代理类
 * @Author luolinyuan
 * @Date 2022/3/30
 **/
@Slf4j
public class RemoteInvoker implements InvocationHandler {
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;
    private Class clazz;

    RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder
            , TransportSelector selector) {
        this.clazz = clazz;
        this.decoder = decoder;
        this.encoder = encoder;
        this.selector = selector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        Response resp = invokeRemote(request);
        if (resp == null || resp.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote:" + resp);
        }
        return resp.getData();
    }

    private Response invokeRemote(Request request) {
        TransportClient client = null;
        Response resp = null;
        try {
            client = selector.select();
            byte[] outBytes = encoder.encode(request);
            InputStream receive = client.write(new ByteArrayInputStream(outBytes));

            byte[] inBytes = IOUtils.readFully(receive, receive.available());
            resp = decoder.decode(inBytes,Response.class);
        } catch (IOException e) {
            log.warn(e.getMessage(),e);
            resp = new Response();
            resp.setCode(1);
            resp.setMessage("RpcClient got error"
                    + e.getClass()
                    + " : " + e.getMessage()
            );
        } finally {
            if (client != null) {

                selector.release(client);
            }
        }
        return resp;
    }
}
