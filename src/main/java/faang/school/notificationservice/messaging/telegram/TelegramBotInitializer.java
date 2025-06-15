package faang.school.notificationservice.messaging.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;

@Component
public class TelegramBotInitializer {
    private final BotKd001 botKd001;

    @Autowired
    public TelegramBotInitializer(BotKd001 bot) {
        this.botKd001 = bot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(botKd001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
