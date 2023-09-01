package faang.school.notificationservice.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.bot.BotConfig;
import faang.school.notificationservice.dto.ExtendedContactDto;
import faang.school.notificationservice.dto.TgContactDto;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NotificationTelegramBot extends TelegramLongPollingBot {
    private BotConfig config;
    private final UserServiceClient userServiceClient;

    public NotificationTelegramBot(BotConfig config, UserServiceClient userServiceClient) {
        super(config.getToken());
        this.config = config;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            checkContactToRegister(update);

        } else if (update.hasMessage() && update.getMessage().hasContact()) {
            offerToSendContact(update);
        }
    }

    private void checkContactToRegister(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText("Push the button for send you phone");
        setPhoneButton(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void offerToSendContact(Update update) {
        Message message = update.getMessage();
        Contact contact = message.getContact();
        Long chatId = message.getChatId();
        String phoneNumber = contact.getPhoneNumber();
        Long userId = userServiceClient.findUserIdByPhoneNumber(phoneNumber);
        initRegistration(userId, chatId, phoneNumber);
    }

    private void setPhoneButton(SendMessage message) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton phoneButton = new KeyboardButton("Send you phone");
        phoneButton.setRequestContact(true);

        row.add(phoneButton);
        keyboardRows.add(row);
        keyboard.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboard);
    }

    private void initRegistration(Long userId, Long chatId, String phone) {
        ExtendedContactDto currentContact = userServiceClient.getUserContact(userId);
        TgContactDto tgContact = new TgContactDto();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        if (phone.equals(currentContact.getPhone())) {
            if (chatId.toString().equals(currentContact.getTgChatId())) {
                message.setText("You already registered");
            } else {
                tgContact.setUserId(userId);
                tgContact.setTgChatId(String.valueOf(chatId));
                userServiceClient.updateUserContact(tgContact);
                message.setText("Registration is successful! You can get telegram-notifications now. Thank you!");
            }
        } else {
            message.setText("Telegram account is not correct! Please, use you actual account for registration!");
        }

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}