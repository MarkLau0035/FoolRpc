package cn.llynsyw.server;

import cn.llynsyw.rpc.ReflectionUtils;
import cn.llynsyw.rpc.Request;

/**
 * @Description 调用具体服务
 * @Author luolinyuan
 * @Date 2022/3/30
 **/
public class ServiceInvoker {
    public Object invoke(ServiceInstance service, Request request) {
        return ReflectionUtils.invoke(service.getTarget(),service.getMethod(),request.getParameters());
    }

}
