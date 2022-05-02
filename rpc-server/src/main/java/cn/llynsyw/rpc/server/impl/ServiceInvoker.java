package cn.llynsyw.rpc.server.impl;

import cn.llynsyw.rpc.common.util.ReflectionUtils;
import cn.llynsyw.rpc.common.bean.ServiceInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 调用具体服务
 * @author luolinyuan
 * @date 2022/3/30
 **/
@Slf4j
@Component
public class ServiceInvoker {
    public Object invoke(ServiceInstance service, Object... parameters) {
        return ReflectionUtils.invoke(service.getTarget(),service.getMethod(),parameters);
    }

}
