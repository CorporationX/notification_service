package faang.school.notificationservice.service.impl;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j2
public class NotificationBotService extends TelegramLongPollingBot {
    private final String botUsername;

    public NotificationBotService(String botToken, String botUsername) {
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
            String errorMessage = String.format("Failed to send message to chatId: %s, error: %s", userId, e.getMessage());
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }
}