package faang.school.notificationservice.config.telegram;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BotConfig {
    @Value("${telegram-bot.name}")
    private String name;
    @Value("${telegram-bot.token}")
    private String token;
}
