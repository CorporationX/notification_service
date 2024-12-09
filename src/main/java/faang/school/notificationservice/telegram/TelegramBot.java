package faang.school.notificationservice.telegram;

import faang.school.notificationservice.exception.NotificationServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.name}")
    private String botName;

    public TelegramBot(@Value("${telegram.bot.token}")String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String responseText;
            if (text.equalsIgnoreCase("/start")) {
                responseText = String.format("Здравствуйте! Я ваш бот. Ваш chat_id = %s", chatId);
                SendMessage message = SendMessage.builder()
                        .chatId(chatId)
                        .text(responseText)
                        .build();
                send(message);
            }
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
            log.info("Message sent to telegram for user with telegram_id: {}", message.getChatId());
        } catch (TelegramApiException e) {
            log.error("Error during sending telegram notification for user with telegram_id: {}", message.getChatId(), e);
            throw new NotificationServiceException(String.format("Error during sending telegram notification for user with telegram_id: %s", message.getChatId()));
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
