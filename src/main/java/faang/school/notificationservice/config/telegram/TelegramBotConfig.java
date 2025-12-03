package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.service.telegram.TelegramBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramBotConfig {

    private final TelegramBot telegramBot;

    @PostConstruct
    public void initBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
            log.info("TelegramBot is registered: {}", telegramBot.getBotUsername());
        } catch (TelegramApiException e) {
            log.error("Failed to register bot");
            throw new RuntimeException(e.getMessage());
        }
    }
}
