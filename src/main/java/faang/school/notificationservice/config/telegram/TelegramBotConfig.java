package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.service.telegram.TelegramBot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@Getter
@RequiredArgsConstructor
@PropertySource(factory = ApplicationTelegramYamlPropertySourceFactory.class, value = "classpath:application-telegram.yaml")
public class TelegramBotConfig {

    private final TelegramBot telegramBot;

    @Bean
    public TelegramBot createBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
            log.info("Bot initial configuration set");
            return telegramBot;
        } catch (TelegramApiException e) {
            log.error("Error while configuring Telegram Bot", e);
            throw new RuntimeException(e);
        }
    }
}
