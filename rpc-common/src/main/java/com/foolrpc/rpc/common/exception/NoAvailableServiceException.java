package com.foolrpc.rpc.common.exception;

/**
 * ServiceNotExistException
 *
 * @author luolinyuan
 * @date 2022/4/28
 **/
public class NoAvailableServiceException extends IllegalStateException {
	public NoAvailableServiceException(String serverName) {
		super("no services are available for " + serverName);
	}
}
