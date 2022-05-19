package com.FoolRpc.rpc.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 表示服务的一个具体服务
 * @author luolinyuan
 * @date 2022/3/29
 **/
@Data
@AllArgsConstructor
public class ServiceInstance {
    private Object target;
    private Method method;
}
