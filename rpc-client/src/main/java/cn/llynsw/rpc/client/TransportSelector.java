package cn.llynsw.rpc.client;

import cn.llynsyw.rpc.Peer;
import cn.llynsyw.rpc.TransportClient;

import java.util.List;

/**
 * @Description 选择哪个server连接
 * @Author luolinyuan
 * @Date 2022/3/30
 **/
public interface TransportSelector {


    /**
     *@Description 初始化selector
     * @param peers 可以连接的server端点信息
     * @param count 表示client与server建立多少个连接
     * @param clazz client实现class
     * @return void
      **/
    void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz);
    /**
     * @param
     * @return cn.llynsyw.rpc.TransportClient
     * @description TODO
      **/
    TransportClient select ();

   /**
    * @param client
    * @description TODO
     **/
    void release(TransportClient client);


    void close();
}
