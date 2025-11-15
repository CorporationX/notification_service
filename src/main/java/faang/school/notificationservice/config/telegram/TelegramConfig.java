package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.exception.TelegramBotIntegrationException;
import faang.school.notificationservice.service.telegram.TelegramServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class TelegramConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramServiceImpl telegramServiceImpl) {
        TelegramBotsApi telegramBotsApi;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramServiceImpl);
        } catch (TelegramApiException e) {
            throw new TelegramBotIntegrationException("Error to register bot Api");
        }
        return telegramBotsApi;
    }
}