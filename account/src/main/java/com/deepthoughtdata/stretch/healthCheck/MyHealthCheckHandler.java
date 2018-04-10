package com.deepthoughtdata.stretch.healthCheck;

import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import static com.netflix.appinfo.InstanceInfo.*;

/**
 * 健康检查处理器
 * 将服务提供者的健康状态传递给eureka服务器
 */
@Component
public class MyHealthCheckHandler implements HealthCheckHandler {

    @Autowired
    private MyHealthIndicator myHealthIndicator;

    @Override
    public InstanceStatus getStatus(InstanceStatus instanceStatus) {
        Status status = myHealthIndicator.health().getStatus();
        if (status.equals(Status.UP)){
            return InstanceStatus.UP;
        } else {
            return InstanceStatus.DOWN;
        }
    }
}
