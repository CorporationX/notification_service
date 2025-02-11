package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramService extends TelegramLongPollingBot implements NotificationService {

    @Value("${telegram.name}")
    private String botUserName;

    @Value("${telegram.token}")
    private String botToken;

    @Override
    public void send(UserDto user, String message) {
        SendMessage botMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getId()))
                .text(message)
                .build();

        try {
            execute(botMessage);
        } catch (TelegramApiException e) {
            log.error("Уведомление не доставлено пользователю с ID {}: {}", user.getId(), e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Я умею только отправлять уведомления");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
