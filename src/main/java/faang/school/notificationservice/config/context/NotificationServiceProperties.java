package faang.school.notificationservice.config.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notification-service")
public class NotificationServiceProperties {
    private String apiVersion;
    private TelegramBot telegramBot;

    @Data
    public static class TelegramBot {
        private String token;
        private String username;
    }
}