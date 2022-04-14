package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsyw.rpc.common.ReflectionUtils;

import java.lang.reflect.Proxy;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/30
 **/
public class RpcClient {
    private RpcClientConfig config;
    private TransportSelector selector;

    public RpcClient() {
        this(new RpcClientConfig());
    }

    public RpcClient(RpcClientConfig config) {
        this.config = config;

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
                new RemoteInvoker(clazz,selector)
        );
    }
}
