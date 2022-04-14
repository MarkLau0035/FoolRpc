package cn.llynsyw.rpc.transport;

/**
 * @Description 1.启动、监听 2.接受请求 3.关闭监听
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
public interface TransportServer {
	/**
	 * 初始化
	 *
	 * @param port    监听端口
	 * @param handler 请求处理器
	 **/
	void init(int port, RequestHandler handler);

	/**
	 * 启动监听
	 **/
	void start();


	/**
	 * 停止监听
	 **/
	void stop();
}
