package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.telegram.TelegramDefaultMessageBuilder;
import faang.school.notificationservice.message.telegram.TelegramWelcomeMessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
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
        sendMessage.setChatId(Long.parseLong(userDto.getTelegramChatId()));
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (message_text.startsWith("/start")) {
                SendMessage message = new SendMessage();
                message.setChatId(chat_id);
                String[] splitMessage = message_text.split(" ");
                long userId = Long.parseLong(splitMessage[1]);
                userServiceClient.saveTelegramChatId(userId, chat_id);
                message.setText(telegramWelcomeMessageBuilder
                        .buildMessage(null, update.getChatMember().toString()));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage message = new SendMessage();
                message.setChatId(chat_id);
                message.setText(telegramDefaultMessageBuilder.buildMessage(null));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
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
}
