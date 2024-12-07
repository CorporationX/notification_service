package faang.school.notificationservice.config.notification;

import lombok.Data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Slf4j
@Configuration
@PropertySource("application.yaml")
public class TelegramConfig {
    @Value("${bot.name}") String botName;
    @Value("${bot.token}") String token;
}
