package faang.school.notificationservice.messaging.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificationTelegramBot extends TelegramLongPollingBot {
    private final String token;
    private final String botName;

    public NotificationTelegramBot(
            @Value("${spring.telegram.token}") String token, 
            @Value("${spring.telegram.bot-name}") String botName) {
        super(token);
        this.token = token;
        this.botName = botName;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Message to user {} was sent.", update.getMessage().getText());
    }

    public void send(String usersChat, String message) {
        SendMessage msg = new SendMessage(usersChat, message);
        try {
            execute(msg);
            log.info("Message to user {} was sent.", usersChat);
        } catch (TelegramApiException e) {
            log.error("Error sending message to {}.", usersChat);
        }
    }
}
