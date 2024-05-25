package faang.school.notificationservice.config.telegram;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class TelegramBotConfig {

    @Value("${telegram-bot.token}")
    String botName;
    @Value("${telegram-bot.token}")
    String botToken;

}
