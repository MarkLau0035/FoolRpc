package cn.llynsyw.rpc;

/**
 * @Description 1.启动、监听 2.接受请求 3.关闭监听
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public interface TransportServer {
    void start();

    void init(int port,RequestHandler handler);

    void stop();
}
