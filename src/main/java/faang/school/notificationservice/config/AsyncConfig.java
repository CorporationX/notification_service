package faang.school.notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.async.scheduled.thread-pool")
public class AsyncConfig {
    private int corePoolSize;
    private int maxPoolSize;

    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(corePoolSize);
        executor.setMaximumPoolSize(maxPoolSize);
        return executor;
    }
}
