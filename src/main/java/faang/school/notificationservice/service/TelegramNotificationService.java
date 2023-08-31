package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.telegram.TelegramDefaultMessageBuilder;
import faang.school.notificationservice.message.telegram.TelegramWelcomeMessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramNotificationService extends TelegramLongPollingBot implements NotificationService {

    @Value("${telegram.botToken}")
    private String botToken;
    @Value("${telegram.botUserName}")
    private String botUserName;
    private final TelegramWelcomeMessageBuilder telegramWelcomeMessageBuilder;
    private final TelegramDefaultMessageBuilder telegramDefaultMessageBuilder;
    private final UserServiceClient userServiceClient;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void sendNotification(String message, UserDto userDto) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userDto.getTelegramChatId());
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        if (messageText.startsWith("/start")) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            String[] splitMessage = messageText.split("\\s+");
            long userId = Long.parseLong(splitMessage[1]);
            userServiceClient.saveTelegramChatId(userId, chatId);
            message.setText(telegramWelcomeMessageBuilder
                    .buildMessage(Locale.getDefault(), update.getChatMember().toString()));
            sendMessage(message);
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(telegramDefaultMessageBuilder.buildMessage(Locale.getDefault()));
            sendMessage(message);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException", e);
        }
    }
}
