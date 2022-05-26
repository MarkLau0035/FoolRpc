package com.foolrpc.rpc.transport.exception;

/**
 * @author guokun
 * @date 2022/5/26 10:09
 */
public class NoStreamException extends Exception{
    public NoStreamException() {
        super("This handler does not have stream!");
    }
}
