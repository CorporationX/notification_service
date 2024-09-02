package faang.school.notificationservice.service.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.name}")
    private String name;
    @Value("${telegram.bot.key}")
    private String token;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        sendMessageByUserId(userId, "This bot is designed to send notifications!");
    }

    @Retryable(value = {TimeoutException.class,
            HttpServerErrorException.class, SocketTimeoutException.class},
            backoff = @Backoff(delay = 2000))
    public void sendMessageByUserId(Long userId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userId.toString());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
