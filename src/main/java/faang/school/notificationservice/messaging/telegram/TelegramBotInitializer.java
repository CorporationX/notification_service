package faang.school.notificationservice.messaging.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;

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
            e.printStackTrace();
        }
    }
}
