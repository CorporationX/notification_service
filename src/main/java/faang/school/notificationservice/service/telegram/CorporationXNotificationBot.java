package faang.school.notificationservice.service.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class CorporationXNotificationBot extends TelegramLongPollingBot {
    @Value("${telegram.bot-username}")
    private String botUsername;
    @Value("${telegram.bot-token}")
    private String botToken;
    private SendMessage message;

    public CorporationXNotificationBot() {
        message = new SendMessage();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            message.setChatId(chatId);
            message.setText("Бот предназначен только для рассылки.");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Сбой в отправке сообщения-ответа пользователю с id: {}", chatId);
                e.printStackTrace();
            }
        }
    }

    public void sendBroadcast(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Сбой в отправке нотификации пользователю с id: {}", chatId);
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
