package faang.school.notificationservice.service;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j2
public class NotificationBot extends TelegramLongPollingBot {
    private final String botUsername;

    public NotificationBot(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    public void sendMessage(String userId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(text);
        try {
            log.info("Attempting to send message to chatId: {}, text: {}", userId, text);
            execute(message);
            log.info("Message sent successfully to chatId: {}", userId);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId: {}, error: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to send Telegram message: " + e.getMessage(), e);
        }
    }
}