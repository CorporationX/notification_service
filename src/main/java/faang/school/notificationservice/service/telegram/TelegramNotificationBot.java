package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.entity.TelegramUser;
import faang.school.notificationservice.repository.TelegramUserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramNotificationBot extends TelegramLongPollingBot {
    private final static String BOT_NAME = "TelegramNotificationBot";
    private final UserServiceClient userServiceClient;
    private final TelegramUserRepository telegramUserRepository;

    @Override
    @Transactional
    @Retryable(retryFor = {FeignException.class, TelegramApiException.class},
            maxAttempts = 5, backoff = @Backoff(delay = 500))
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (message_text.equals("/start")) {
                String response = "Для получения оповещений предоставьте,пожалуйста, ваш id.";
                sendMessage(chat_id, response);
            } else {
                subscribeToNotifications(message_text, chat_id);
            }
        }
    }

    @Retryable(retryFor = TelegramApiException.class, maxAttempts = 5, backoff = @Backoff(delay = 500))
    public void sendNotification(long chat_id, String notification) {
        sendMessage(chat_id, notification);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return "7063955480:AAFT1nYPcT1SAv8zVQj0Mz-3i2ln7PaegQg";
    }

    private void subscribeToNotifications(String message_text, long chat_id) {
        if (isInteger(message_text)) {
            long userId = Long.parseLong(message_text);
            if (userServiceClient.isUserExists(userId)) {
                String response = "Вы подписались на получение оповещений";
                TelegramUser telegramUser = TelegramUser.builder().chatId(chat_id).userId(userId).build();
                if (!telegramUserRepository.existsByChatId(chat_id)) {
                    telegramUserRepository.save(telegramUser);
                    sendMessage(chat_id, response);
                } else {
                    String response2 = "Вы уже подписались на получение оповещений";
                    sendMessage(chat_id, response2);
                }
            } else {
                String response = "Ваш аккаунт не найден";
                sendMessage(chat_id, response);
            }
        } else {
            String response = "Предоставьте корректный id";
            sendMessage(chat_id, response);
        }
    }


    private void sendMessage(long chat_id, String response) {
        SendMessage message = SendMessage.builder()
                .chatId(chat_id)
                .text(response).build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Message has not sent " + e);
        }
    }

    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-') {
                if (str.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
