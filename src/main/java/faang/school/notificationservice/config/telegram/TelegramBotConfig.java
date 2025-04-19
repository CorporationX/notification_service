package faang.school.notificationservice.config.telegram;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TelegramBotConfig {

    @Value("${TELEGRAM_BOT_USERNAME}")
    private String username;

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String token;
}

