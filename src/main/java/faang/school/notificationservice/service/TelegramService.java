package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot implements NotificationService {
    private TelegramBotConfig telegramBotConfig;

    @Override
    public void send(UserDto user, String messageText) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(user.getId());
            message.setText(messageText);
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getBotToken();
    }

}
