package faang.school.notificationservice.config.telegramBotConfig;

import faang.school.notificationservice.service.telegram.TelegramNotificationBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {
    private final TelegramNotificationBot telegramNotificationBot;

    @Bean
    public void registeringBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramNotificationBot);
        } catch (TelegramApiException e) {
            log.error("Бот не зарегистрирован: " + e);
        }
    }
}
