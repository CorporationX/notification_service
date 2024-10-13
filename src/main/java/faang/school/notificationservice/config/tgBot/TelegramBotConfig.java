package faang.school.notificationservice.config.tgBot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.yaml")
public class TelegramBotConfig {
    @Value("${bot.name}") String botName;
    @Value("${bot.token}") String token;
}
