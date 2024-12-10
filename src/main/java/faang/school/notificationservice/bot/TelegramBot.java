package faang.school.notificationservice.bot;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.UserForNotificationDto;
import feign.FeignException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final UserServiceClient userServiceClient;

    @Value("${spring.telegram.bot.username}")
    private String botUsername;

    @Value("${spring.telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            log.info("The bot has received a message with text {}", update.getMessage().getText());
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            User user = message.getFrom();
            String languageCode = user.getLanguageCode();

            if (message.hasText()) {
                if ("/start".equals(message.getText())) {
                    checkIfTheUserIsSubscriber(chatId);
                }
                if ("/stop".equals(message.getText())) {
                    unsubscribingUserFromNotifications(chatId);
                }
                if ("No".equals(message.getText())) {
                    sendTextMessage(chatId, "You chose not to subscribe. If you want to subscribe again, enter /start");
                }
            }
            if (message.hasContact()) {
                processingOfContactData(chatId, message.getContact());
            }
        }
    }

    private void checkIfTheUserIsSubscriber(Long chatId) {
        ContactDto contactDto = userServiceClient.getContactByNumber(String.valueOf(chatId));
        if (contactDto != null) {
            sendTextMessage(chatId, "You have already subscribed to notifications");
        } else {
            sendRequestContactMessage(chatId);
        }
    }

    private void sendRequestContactMessage(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardButton contactButton = new KeyboardButton();
        contactButton.setText("Subscribe on notifications");
        contactButton.setRequestContact(true);

        keyboardRow.add(contactButton);
        keyboardRow.add(new KeyboardButton("No"));
        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

        SendMessage message = new SendMessage();
        message.setText("Do you want to receive notifications in a telegram?");
        message.setChatId(chatId);
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
            log.debug("Telegram bot request contact: {}", message.getText());
        } catch (TelegramApiException e) {
            log.error("Telegram bot failed to request contact: {}", message.getText(), e);
        }
    }

    private void processingOfContactData(Long chatId, Contact contact) {
        log.info("Processing of contact data for chatId: {} and contact: {}", chatId, contact);
        UserForNotificationDto user = checkThatUserIsEmpty(chatId, contact);

        ContactDto contactDto = ContactDto.builder()
                .contact(String.valueOf(chatId))
                .userId(user.id())
                .type(ContactDto.ContactType.TELEGRAM)
                .build();
        userServiceClient.createContact(contactDto);
        log.debug("Contact created: {}", contactDto);

        sendTextMessage(chatId, "Now this telegram bot will send you notifications");
    }

    private void sendTextMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        try {
            execute(sendMessage);
            log.debug("Telegram bot sent message: {}", text);
        } catch (TelegramApiException e) {
            log.error("Telegram bot failed to send message: {}", text, e);
        }
    }

    private void unsubscribingUserFromNotifications(Long chatId) {
        ContactDto contactDto = userServiceClient.getContactByNumber(String.valueOf(chatId));
        if (contactDto != null) {
            userServiceClient.deleteContactByNumber(String.valueOf(chatId));
            sendTextMessage(chatId, "You have unsubscribed from notifications. If you want to subscribe again, enter /start");
        } else {
            sendTextMessage(chatId, "You are not subscribed to notifications. To subscribe, enter /start");
        }
    }

    private UserForNotificationDto checkThatUserIsEmpty(Long chatId, Contact contact) {
        try {
            log.debug("Telegram bot checked that user is empty");
            String userPhone = contact.getPhoneNumber();
            return userServiceClient.getUserByPhone(userPhone);
        } catch (FeignException e) {
            log.error("Telegram bot failed to check that user is not a user of the platform", e);
            sendTextMessage(chatId, """
                     The user with the phone number listed in your telegram profile has not been found.\s
                     Please update the phone number in your profile on the platform or register.
                    \s""");
            throw new IllegalArgumentException("Telegram bot failed to check that user is not a user of the platform", e);
        }
    }
}
