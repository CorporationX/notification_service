package faang.school.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class ExecutorConfig {

    @Value("${application.thread.pool-size}")
    private int corePoolSize;

    @Bean
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(corePoolSize);
    }
}
