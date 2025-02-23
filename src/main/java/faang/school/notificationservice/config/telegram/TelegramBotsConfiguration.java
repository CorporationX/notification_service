package faang.school.notificationservice.config.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@RequiredArgsConstructor
public class TelegramBotsConfiguration {

    private final BotProperties botProperties;

    @Bean
    TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botProperties.getToken());
    }
}
