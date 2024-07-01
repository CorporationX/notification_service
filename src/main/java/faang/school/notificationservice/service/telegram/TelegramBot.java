package faang.school.notificationservice.service.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;

    @Async("telegramBotPool")
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMessage(chatId, "Добро пожаловать в CorporationX");
            } else if (messageText.equals("/help")) {
                sendMessage(chatId, "Список доступных команд: \n /start - запуск бота\n /help - получить список всех команд");
            } else {
                sendMessage(chatId, "Извините, я не могу дать ответ на ваше сообщение. Вы ввели неизвестную мне команду");
            }
        }
    }

    @Async("telegramBotPool")
    public void sendNotification(long chatId, String message) {
        sendMessage(chatId, message);
    }

    @Override
    public String getBotUsername() {
        return config.botName;
    }

    @Override
    public String getBotToken() {
        return config.botToken;
    }

    private void sendMessage(long chatId, String message) {
        SendMessage notification = new SendMessage();
        notification.setChatId(chatId);
        notification.setText(message);

        try {
            execute(notification);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
