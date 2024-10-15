package faang.school.notificationservice.bot;

import faang.school.notificationservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final UserServiceClient userServiceClient;

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
        if (update.hasMessage()) {
            Message updateMessage = update.getMessage();
            Chat chat = updateMessage.getChat();
            if (chat.isGroupChat()) {
                log.warn("Attempt to use bot from group chat, chat id = {}", chat.getId());
                sendGroupChatWarning(updateMessage.getChatId().toString());
            }

            User messageFrom = updateMessage.getFrom();
            String telegramUserId = messageFrom.getId().toString();
            String telegramUserName = messageFrom.getUserName();
            userServiceClient.updateTelegramUserId(telegramUserName, telegramUserId);
        }
    }

    private void sendGroupChatWarning(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("This bot doesn't work with group chats");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException was occurred while send warn to group chat", e);
        }
    }
}

