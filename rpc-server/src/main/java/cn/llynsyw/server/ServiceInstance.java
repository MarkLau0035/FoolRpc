package cn.llynsyw.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @Description 表示服务的一个具体服务
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
@Data
@AllArgsConstructor
public class ServiceInstance {
    private Object target;
    private Method method;
}
