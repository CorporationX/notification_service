package faang.school.notificationservice.config.tg;

import faang.school.notificationservice.exception.TelegramBotInitializationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramBotConfig {

    private final TgNotificationBot tgNotificationBot;

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        TelegramBotsApi telegramBotsApi;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(tgNotificationBot);
        } catch (TelegramApiException e) {
            log.error("Failed to initialize TelegramBotsApi or register bot: {}",
                    tgNotificationBot.getBotUsername(), e);
            throw new TelegramBotInitializationException("Failed to initialize TelegramBotsApi or register bot: "
                    + tgNotificationBot.getBotUsername(), e);
        }
        return telegramBotsApi;
    }
}

