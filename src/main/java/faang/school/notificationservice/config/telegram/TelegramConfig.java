package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Component
@RequiredArgsConstructor
public class TelegramConfig {

    private final TelegramNotificationService telegramService;

    @EventListener({ApplicationStartup.class})
    public void init() throws TelegramApiException{
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramService);
    }
}
