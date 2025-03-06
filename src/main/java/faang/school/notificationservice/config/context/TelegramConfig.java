package faang.school.notificationservice.config.context;

import faang.school.notificationservice.properties.NotificationServiceProperties;
import faang.school.notificationservice.service.impl.NotificationBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {
    private final NotificationServiceProperties notificationServiceProperties;

    @Bean
    public NotificationBotService myTelegramBot() {
        return new NotificationBotService(notificationServiceProperties.getTelegramBot().getToken(),
                notificationServiceProperties.getTelegramBot().getUsername());
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(NotificationBotService notificationBotService) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(notificationBotService);
        return botsApi;
    }
}