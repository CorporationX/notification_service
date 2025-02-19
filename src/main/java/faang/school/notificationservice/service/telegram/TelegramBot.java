package faang.school.notificationservice.service.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.name}")
    private String botUserName;

    @Value("${telegram.token}")
    private String botToken;

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

            var message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Я умею только отправлять уведомления")
                    .build();

            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке ответа Пользователю с ID {}: {}", chatId, e.getMessage());
            }
        }
    }
}

