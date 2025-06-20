package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.service.TelegramService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Getter
@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotConfig {

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.enable}")
    private boolean enable;

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramService telegramService) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        if (enable) {
            botsApi.registerBot(telegramService);
        }
        return botsApi;
    }
}