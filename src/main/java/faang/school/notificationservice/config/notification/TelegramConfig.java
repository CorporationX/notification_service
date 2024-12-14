package faang.school.notificationservice.config.notification;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Data
@Configuration
@PropertySource("application.yaml")
public class TelegramConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;

    @Bean
    public Executor telegramBotExecutor(@Value("${telegram-thread-pool-size}") int threadPoolSize) {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
