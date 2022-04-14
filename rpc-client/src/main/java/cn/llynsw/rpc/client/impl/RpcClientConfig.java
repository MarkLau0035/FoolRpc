package cn.llynsw.rpc.client.impl;

import cn.llynsw.rpc.client.TransportSelector;
import cn.llynsyw.rpc.Peer;
import cn.llynsyw.rpc.TransportClient;
import cn.llynsyw.rpc.impl.HttpTransportClient;
import com.llynsyw.rpc.Decoder;
import com.llynsyw.rpc.Encoder;
import com.llynsyw.rpc.impl.JSONDecoder;
import com.llynsyw.rpc.impl.JSONEncoder;
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
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass =
            RandomTransportSelector.class;
    private int connectCount = 1;
    private List<Peer> servers = Arrays.asList(new Peer("127.0.0.1",3000));
}
