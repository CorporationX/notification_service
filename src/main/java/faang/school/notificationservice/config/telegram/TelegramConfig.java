package faang.school.notificationservice.config.telegram;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TelegramConfig {
    @Value("${telegram.bot.username}")
    String username;
    @Value("${telegram.bot.token}")
    String token;
}
