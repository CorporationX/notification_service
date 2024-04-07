package faang.school.notificationservice.config.notification.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    @Value("${telegram.bot-token}")
    private String token;
    @Value("${telegram.bot-username}")
    private String username;

    @Bean
    public String telegramBotToken () {
        return token;
    }

    @Bean
    public String telegramBotUsername () {
        return username;
    }
}
