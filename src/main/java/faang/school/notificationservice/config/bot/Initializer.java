package faang.school.notificationservice.config.bot;

import faang.school.notificationservice.NotificationTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class Initializer {
    @Autowired
    BotConfig config;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        NotificationTelegramBot bot = new NotificationTelegramBot(config.token, config);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot((LongPollingBot) bot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

