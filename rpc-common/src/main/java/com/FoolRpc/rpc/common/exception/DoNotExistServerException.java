package com.FoolRpc.rpc.common.exception;

/**
 * ServiceNotExistException
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public class DoNotExistServerException extends IllegalStateException {
	public DoNotExistServerException(String serverName) {
		super("The service " + serverName + "does not exist");
	}
}
