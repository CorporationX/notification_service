package faang.school.notificationservice.config.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Value("${telegram-bot.name}")
    private String name;
    @Value("${telegram-bot.token}")
    private String token;
}
