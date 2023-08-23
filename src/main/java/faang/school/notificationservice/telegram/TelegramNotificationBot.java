package faang.school.notificationservice.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramNotificationBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.name}")
    private String name;

    public TelegramNotificationBot(@Value("${telegram.bot.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!(update.hasMessage() && update.getMessage().hasText())) return;
        SendMessage message = SendMessage.builder()
                .text("I apologize, but my sole purpose is to deliver notifications.")
                .chatId(update.getMessage().getChatId())
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("An error occurred while trying to send a message via Telegram bot: {}", e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    // @Async Не уверен, нужно ли
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendMessage(long chat_id, String rowMessage) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chat_id)
                .text(rowMessage)
                .build();
        execute(message);
    }
}

