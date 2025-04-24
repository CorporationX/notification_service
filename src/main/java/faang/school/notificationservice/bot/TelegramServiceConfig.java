package faang.school.notificationservice.bot;

import faang.school.notificationservice.properties.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramServiceConfig {

    private final TelegramProperties telegramProperties;

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramLongPollingBot bot) throws TelegramApiException {
        TelegramBotsApi api;
        try {
            api = new TelegramBotsApi(DefaultBotSession.class);
            if (telegramProperties.getToken() != null && telegramProperties.getToken().contains("YOUR_TELEGRAM_BOT_TOKEN")) {
                log.warn("Используются тестовые настройки, регистрация webhook-а пропущена.");
            } else {
                try {
                    api.registerBot(bot);
                } catch (TelegramApiException e) {
                    if (e.getMessage() != null && e.getMessage().contains("Error removing old webhook")) {
                        log.warn("Ошибка удаления старого webhook-а, пропускаем: {}", e.getMessage());
                    } else {
                        throw e;
                    }
                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка инициализации TelegramBotsApi", e);
        }
        return api;
    }

}