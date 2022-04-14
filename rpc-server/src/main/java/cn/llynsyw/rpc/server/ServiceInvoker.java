package cn.llynsyw.rpc.server;

import cn.llynsyw.rpc.common.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Description 调用具体服务
 * @Author luolinyuan
 * @Date 2022/3/30
 **/
@Slf4j
public class ServiceInvoker {
    public Object invoke(ServiceInstance service, Object... parameters) {
        log.debug(service.toString(), Arrays.stream(parameters).iterator().toString());
        return ReflectionUtils.invoke(service.getTarget(),service.getMethod(),parameters);
    }

}
