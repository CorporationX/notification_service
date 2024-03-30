package faang.school.notificationservice.config.Telegram;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BotConfig {
    @Value("${tg-bot.name}")
    private String name;
    @Value("${tg-bot.token}")
    private String token;
}
