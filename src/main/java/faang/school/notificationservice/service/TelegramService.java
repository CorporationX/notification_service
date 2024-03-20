package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.telegram.TelegramBotConfig;
import faang.school.notificationservice.dto.TgContactDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService extends TelegramLongPollingBot implements NotificationService {
    private final TelegramBotConfig telegramBotConfig;
    private final UserServiceClient userServiceClient;

    @Override
    public void send(UserDto user, String messageText) {
        try {
            TgContactDto tgContactDto = userServiceClient.getTgContact(user.getId());
            SendMessage message = new SendMessage();
            message.setChatId(tgContactDto.getChatId());   //"417512145"
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
