package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.service.telegram.TelegramServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramServiceImpl telegramServiceImpl) {
        TelegramBotsApi botsApi;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        try {
            botsApi.registerBot(telegramServiceImpl);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return botsApi;
    }
}