package faang.school.notificationservice.config.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "executor.email")
public class AsyncMailExecutorProperties {

    private int threadCoreSize;
    private int maxPoolSize;
    private int queueCapacity;
}
