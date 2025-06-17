package faang.school.notificationservice.messaging.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelegramBotInitializer {
    private final NotificationTelegramBot notificationTelegramBot;

    @Autowired
    public TelegramBotInitializer(NotificationTelegramBot notificationTelegramBot) {
        this.notificationTelegramBot = notificationTelegramBot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(notificationTelegramBot);
        } catch (Exception e) {
            log.error("Error initializing Telegram bot: {}", e.getMessage(), e);
            throw new RuntimeException("Error initializing Telegram bot", e);
        }
    }
}
