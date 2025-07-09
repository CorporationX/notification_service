package faang.school.notificationservice.config.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {
    @Value("${telegram.bot.notification.token}")
    private String botToken;

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botToken);
    }
}
