package faang.school.notificationservice.config.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class EmailConfig {

    @Bean
    public Executor mailExecutor(@Value("${email-service.thread-pool-size}") int threadPoolSize) {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
