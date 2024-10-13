package faang.school.notificationservice.bot;

import faang.school.notificationservice.service.telegram.TelegramContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramContactService contactService;

    @Value("telegram-bot.welcomeMessage")
    private String welcomeMessage;

    @Value("${telegram-bot.botName}")
    private String botName;

    public TelegramBot(@Value("${telegram-bot.token}") String botToken,
                       TelegramContactService contactService) {
        super(botToken);
        this.contactService = contactService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String messageText;
        if (Objects.equals(message.getText(), "/start")) {
            String username = "@" + message.getChat().getUserName();
            contactService.addChatIdForUser(username, chatId);
            messageText = welcomeMessage;
        } else {
            messageText = message.getText();
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}