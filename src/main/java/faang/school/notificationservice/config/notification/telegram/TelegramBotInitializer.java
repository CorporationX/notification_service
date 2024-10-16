package faang.school.notificationservice.config.notification.telegram;

import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotInitializer {

    private final TelegramService telegramService;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            telegramBotsApi.registerBot(telegramService);
        } catch (TelegramApiException e) {
            log.error("Telegram bot couldn't wake up");
            throw new RuntimeException(e);
        }
    }
}
