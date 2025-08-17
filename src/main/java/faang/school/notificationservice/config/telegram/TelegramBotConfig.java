package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.exception.TelegramBotRegistrationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramServiceBot telegramServiceBot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramServiceBot);
            return botsApi;
        } catch (TelegramApiException e) {
            throw new TelegramBotRegistrationException("Failed to register "
                    + "telegram bot", e.getCause());
        }
    }
}
