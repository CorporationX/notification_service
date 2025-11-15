package faang.school.notificationservice.config.telegrambot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
@Getter
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.welcome-message}")
    private String welcomeMessage;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }

    @Retryable(
            retryFor = TelegramApiException.class,
            maxAttemptsExpression = "${telegram.notification.max-retry-attempts:3}",
            backoff = @Backoff(delayExpression = "${telegram.notification.retry-delay-ms:1000}")
    )
    public void sendNotification(Long chatId, String message) {
       sendMessage(chatId, message);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            sendMessage(chatId, welcomeMessage);
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text).build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message to user", e);
            throw new RuntimeException(e);
        }
    }
}