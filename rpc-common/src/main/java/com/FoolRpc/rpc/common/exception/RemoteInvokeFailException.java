package com.FoolRpc.rpc.common.exception;

/**
 * RemoteInvokeFailException
 *
 * @author luolinyuan
 * @date 2022/5/19
 **/
public class RemoteInvokeFailException extends IllegalStateException {
	public RemoteInvokeFailException(String method, String message) {
		super("fail to invoke " + method + " details: " + method);
	}
}
