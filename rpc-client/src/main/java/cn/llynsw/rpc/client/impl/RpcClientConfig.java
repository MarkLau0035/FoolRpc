package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsyw.rpc.transport.Peer;
import cn.llynsyw.rpc.transport.TransportClient;
import cn.llynsyw.rpc.transport.impl.HttpTransportClient;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/30
 **/
@Data
public class RpcClientConfig {
    private Class<? extends TransportClient> transportClass = HttpTransportClient.class;
    private Class<? extends TransportSelector> selectorClass =
            RandomTransportSelector.class;
    private int connectCount = 1;
    private List<Peer> servers = Arrays.asList(new Peer("127.0.0.1",3000));
}
