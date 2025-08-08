package faang.school.notificationservice.config.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramServiceBot telegramServiceBot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramServiceBot);
            log.info("Telegram bot registered successfully");
            return botsApi;
        } catch (TelegramApiException e) {
            log.error("Failed to register telegram bot", e);
            throw new RuntimeException("Failed to register telegram bot", e);
        }
    }
}