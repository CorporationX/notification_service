package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.exception.ServiceCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationBot extends TelegramLongPollingBot {

    @Value("${telegram.notification-bot.username}")
    private String botUsername;

    @Value("${telegram.notification-bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            if (chatId == null) {
                return;
            }
            String text = "This chat's id is %d".formatted(chatId);
            sendMessage(chatId, text);
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
            log.info("Message sent in Telegram chat with ID {}: {}", chatId, text);
        } catch (TelegramApiException e) {
            throw new ServiceCallException("Unable to send message in Telegram chat with ID %d".formatted(chatId), e);
        }
    }
}
