package faang.school.notificationservice.telegram.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;


@Component
@RequiredArgsConstructor
public class NotificationTelegramBot implements SpringLongPollingBot {
    private final NotificationBotUpdateConsumer consumer;
    @Value("${telegram.bot.notification.name}")
    private final String name;
    @Value("${telegram.bot.notification.token}")
    private final String token;

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return consumer;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
