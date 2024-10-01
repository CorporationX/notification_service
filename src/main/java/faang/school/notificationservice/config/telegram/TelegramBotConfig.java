package faang.school.notificationservice.config.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramBotConfig {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    TelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }
}
