package faang.school.notificationservice.service.telegram;

import lombok.Getter;
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
    private String username;
    private String token;
    private boolean enable;

    @Bean
    public TelegramBotsApi telegramBotsApi(NotificationBot notificationBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        if (enable) {
            botsApi.registerBot(notificationBot);
        }
        return botsApi;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}