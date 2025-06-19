package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramService extends TelegramLongPollingBot implements NotificationService {
    private final String botUsername;

    public TelegramService(@Value("${telegram.bot.token}") String botToken,
                           @Value("${telegram.bot.username}") String botUsername,
                           @Value("${telegram.bot.enable}") boolean botEnabled) {
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

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void send(UserDto user, String message) {
        if (user == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }

        try {
            long telegramChatId = user.getId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(telegramChatId));
            sendMessage.setText(message);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new NotificationFailedException(
                    String.format("Failed to send Telegram notification to user %d", user.getId()),
                    e
            );
        }
    }
}