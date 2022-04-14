package cn.llynsyw.rpc;

import java.io.InputStream;

/**
 * @Description 1.创建连接 2.发送数据 等待 3.关闭连接
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public interface TransportClient {
    void connect(Peer peer);

    InputStream write(InputStream data);
    void close();
}
