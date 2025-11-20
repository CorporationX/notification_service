package faang.school.notificationservice.config.telegrambot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class BotConfig {

    public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
            log.info("Telegram bot registered successfully");
            return botsApi;
        } catch (TelegramApiException e) {
            log.error("Error registering bot: {}", e.getMessage());
            throw new RuntimeException("Failed to register bot", e);
        }
    }
}
