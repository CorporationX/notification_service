package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.service.notification.impl.telegram.TelegramBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramInitializer {
    private final TelegramBot telegramBot;

    @PostConstruct
    public void onApplicationEvent() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            log.info("Работа Телеграм бота запущена");
        } catch (TelegramApiException e) {
            log.error("Ошибка при регистрации Телеграм бота: {}", e.getMessage());
        }
    }
}
