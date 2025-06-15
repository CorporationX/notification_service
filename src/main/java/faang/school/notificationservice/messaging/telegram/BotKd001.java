package faang.school.notificationservice.messaging.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BotKd001 extends TelegramLongPollingBot {
    public BotKd001() {
        super("");
    }

    @Override
    public String getBotUsername() {
        return "kd_001_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            User telegramUser = update.getMessage().getFrom();

            SendMessage replyMessage = new SendMessage(String.valueOf(chat_id), "Hello, " + message_text);
            log.info("Chat id is {}. User name is {}.", chat_id, telegramUser.getUserName());       

            try {
                execute(replyMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(UserDto user, String message) {
        ContactDto contact = user.getContacts().stream()
            .filter(cont -> cont.getType().equals("TELEGRAM"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(String.format(
                "The user %d is not registered with the telegram bot.", user.getId())
            ));
        SendMessage msg = new SendMessage(contact.getContact(), "Hello, " + message);
        try {
            execute(msg);
            log.info("Message to user {} was sent.", contact.getContact());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
