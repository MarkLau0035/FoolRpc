package cn.llynsyw.rpc.common.exception;

/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public class RpcClientInitializeException extends Exception{
	public RpcClientInitializeException() {
		super("No service node specified");
	}
}
