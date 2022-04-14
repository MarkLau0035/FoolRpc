package cn.llynsyw.server;

import cn.llynsyw.rpc.ReflectionUtils;
import cn.llynsyw.rpc.Request;
import cn.llynsyw.rpc.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 管理rpc暴露的服务
 * @Author luolinyuan
 * @Date 2022/3/29
 **/
@Slf4j
public class ServiceManager {
    private Map<ServiceDescriptor,ServiceInstance> service;

    public ServiceManager(){
        this.service = new ConcurrentHashMap<>();
    }

    public  <T> void register(Class<T> interfaceClass,T bean){
        Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);
        for (Method method : methods) {
            ServiceInstance sis = new ServiceInstance(bean,method);
            ServiceDescriptor sdp = ServiceDescriptor.from(interfaceClass,method);
            service.put(sdp,sis);
            log.info("register service: {} {}",sdp.getClazz(),sdp.getMethod());
        }
    }

    public ServiceInstance lookup(Request request) {
        ServiceDescriptor sdp = request.getService();
        return service.get(sdp);
    }
}
