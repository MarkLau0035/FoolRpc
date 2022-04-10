package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsyw.rpc.ReflectionUtils;
import com.llynsyw.rpc.Decoder;
import com.llynsyw.rpc.Encoder;

import java.lang.reflect.Proxy;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/30
 **/
public class RpcClient {
    private RpcClientConfig config;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RpcClient() {
        this(new RpcClientConfig());
    }

    public RpcClient(RpcClientConfig config) {
        this.config = config;

        this.encoder = ReflectionUtils.newInstance(this.config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(this.config.getDecoderClass());
        this.selector = ReflectionUtils.newInstance(this.config.getSelectorClass());

        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClass());
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {clazz},
                new RemoteInvoker(clazz,encoder,decoder,selector)
        );
    }
}
