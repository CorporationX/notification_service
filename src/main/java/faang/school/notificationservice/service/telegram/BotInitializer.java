package faang.school.notificationservice.service.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class BotInitializer {
    private final CorporationXNotificationBot notificationBot;

    @PostConstruct
    private void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(notificationBot);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации бота.");
        }
    }
}
