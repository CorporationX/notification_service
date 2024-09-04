package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TgNotificationBot extends TelegramLongPollingBot {
    private final UserService userService;
    private final Map<Long, Boolean> awaitingUserId = new ConcurrentHashMap<>();
    @Value("${telegram.bot.username}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (awaitingUserId.getOrDefault(chatId, false)) {
                handleUserIdInput(chatId, messageText);
            } else {
                handleCommands(chatId, messageText, update.getMessage().getChat().getFirstName());
            }
        }
    }


    public void sendTextMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void startCommandReceiver(long chatId, String name) {
        awaitingUserId.put(chatId, true);
        String answer = "Привет, " + name + "! Пожалуйста, введите свой ID:";
        sendTextMessage(chatId, answer);
    }

    private void handleUserIdInput(long chatId, String userId) {
        awaitingUserId.put(chatId, false);
        userService.putChatIdInUser(Long.parseLong(userId), chatId);
        sendTextMessage(chatId, "Ваш ID сохранен: " + userId);
    }

    private void handleCommands(long chatId, String messageText, String name) {
        switch (messageText) {
            case "/start":
                startCommandReceiver(chatId, name);
                break;
            default:
                sendTextMessage(chatId, "Неизвестная команда. Пожалуйста, используйте /start для начала.");
                break;
        }
    }
}