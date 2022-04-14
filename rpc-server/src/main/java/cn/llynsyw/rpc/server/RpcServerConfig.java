package cn.llynsyw.rpc.server;

import cn.llynsyw.rpc.transport.TransportServer;
import cn.llynsyw.rpc.transport.impl.HttpTransportSever;
import lombok.Data;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HttpTransportSever.class;
    private  int port = 3000;
}
