package faang.school.notificationservice.config.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotConfig {
    String name;
    String token;
}
