package faang.school.notificationservice.config.thread.pool;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync(proxyTargetClass = true)
@RequiredArgsConstructor
public class ExecutorConfig {

    @Value("${server.main-thread-pool-size}")
    private int threadPoolSize;

    @Bean
    public ExecutorService mainExecutorService() {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
