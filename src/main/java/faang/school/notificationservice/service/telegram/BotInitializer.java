package faang.school.notificationservice.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotInitializer {
    private final CorporationXNotificationBot notificationBot;

    @EventListener
    private void registerBot(ContextRefreshedEvent event) {
        try {
            log.info("Старт инициализации бота.");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(notificationBot);
        } catch (Exception e) {
            log.error("Ошибка инициализации бота.");
            throw new RuntimeException("Ошибка инициализации бота.");
        }
    }
}
