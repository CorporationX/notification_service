package faang.school.notificationservice.config.telegram;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram-bot.token}")
    private String token;
    @Value("${telegram-bot.name}")
    private String name;
}
