package com.FoolRpc.rpc.common.exception;

/**
 * RegisterFailException
 *
 * @author luolinyuan
 * @date 2022/5/18
 **/
public class RegisterFailException extends IllegalStateException{
	public RegisterFailException() {
		super("Registration failed due to null pointer");
	}
}
