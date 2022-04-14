package cn.llynsyw.rpc;

import lombok.Data;

/**
 * 表示RPC的一个请求
 *
 * @author luolinyuan
 */
@Data
public class Request {
    private ServiceDescriptor service;
    private Object[] parameters;
}
