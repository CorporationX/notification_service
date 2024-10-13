package faang.school.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@EnableAsync
public class TaskExecutorConfig {

    private final TaskExecutorProperties taskExecutorProperties;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(taskExecutorProperties.getThreadNamePrefix());
        return executor;
    }
}
