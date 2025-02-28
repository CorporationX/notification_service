package faang.school.notificationservice.config.context;

import faang.school.notificationservice.service.NotificationBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {
    private final TelegramBotProperties telegramBotProperties;

    @Bean
    public NotificationBot myTelegramBot() {
        return new NotificationBot(telegramBotProperties.getToken(), telegramBotProperties.getUsername());
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(NotificationBot notificationBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(notificationBot);
        return botsApi;
    }
}