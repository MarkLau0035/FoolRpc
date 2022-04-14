package cn.llynsyw.server;

import cn.llynsyw.rpc.TransportServer;
import cn.llynsyw.rpc.impl.HttpTransportSever;
import com.llynsyw.rpc.Decoder;
import com.llynsyw.rpc.Encoder;
import com.llynsyw.rpc.impl.JSONDecoder;
import com.llynsyw.rpc.impl.JSONEncoder;
import lombok.Data;

/**
 * @Description TODO
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HttpTransportSever.class;
    private Class<? extends Encoder> encoder = JSONEncoder.class;
    private Class<? extends Decoder> decoder = JSONDecoder.class;
    private  int port = 3000;
}
