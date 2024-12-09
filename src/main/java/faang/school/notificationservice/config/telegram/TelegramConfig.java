package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.exception.NotificationServiceException;
import faang.school.notificationservice.telegram.TelegramBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramConfig {
    private final TelegramBot telegramBot;

    @PostConstruct
    public void createBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            log.error("Telegram bot initialization error", e);
            throw new NotificationServiceException("Telegram bot initialization error");
        }
    }
}
