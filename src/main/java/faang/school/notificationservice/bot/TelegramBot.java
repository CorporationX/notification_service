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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class TelegramBot extends TelegramLongPollingBot {
    private final UserServiceClient userServiceClient;
    private final TelegramBotMessageProperties properties;

    @Value("${spring.telegram.bot.username}")
    private String botUsername;

    @Value("${spring.telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            log.info("The bot has received a message with text {}", update.getMessage().getText());
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if (message.hasText()) {
                if (properties.getStartMessage().equals(message.getText())) {
                    checkIfTheUserIsSubscriber(chatId);
                }
                if (properties.getStopMessage().equals(message.getText())) {
                    unsubscribingUserFromNotifications(chatId);
                }
                if (properties.getRejectionMessage().equals(message.getText())) {
                    sendTextMessage(chatId, properties.getUserChoseNotSubscribedMessage());
                }
            }
            if (message.hasContact()) {
                processingOfContactData(chatId, message.getContact());
            }
        }
    }

    private void checkIfTheUserIsSubscriber(Long chatId) {
        ContactDto contactDto = userServiceClient.getContactByNumber(String.valueOf(chatId));
        log.debug("Check if the user is a subscriber: {}", contactDto);
        if (contactDto != null) {
            sendTextMessage(chatId, properties.getUserAlreadySubscribedMessage());
        } else {
            sendRequestContactMessage(chatId);
        }
    }

    private void sendRequestContactMessage(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardButton contactButton = new KeyboardButton();
        contactButton.setText(properties.getSubscribeButtonText());
        contactButton.setRequestContact(true);

        keyboardRow.add(contactButton);
        keyboardRow.add(new KeyboardButton("No"));
        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

        SendMessage message = new SendMessage();
        message.setText(properties.getOfferToSubscribeMessage());
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

        sendTextMessage(chatId, properties.getSuccessSubscribedMessage());
    }

    public void sendTextMessage(Long chatId, String text) {
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
            log.info("The user {} has unsubscribed from notifications in the telegram bot", chatId);
            sendTextMessage(chatId, properties.getSuccessUnsubscribedMessage());
        } else {
            sendTextMessage(chatId, properties.getUserNotSubscribedMessage());
        }
    }

    private UserForNotificationDto checkThatUserIsEmpty(Long chatId, Contact contact) {
        try {
            log.debug("Telegram bot checked that user is empty");
            String userPhone = contact.getPhoneNumber();
            return userServiceClient.getUserByPhone(userPhone);
        } catch (FeignException e) {
            log.error("Telegram bot failed to check that user is not a user of the platform", e);
            sendTextMessage(chatId, properties.getUserNotFoundMessage());
            throw new IllegalArgumentException("Telegram bot failed to check that user is not a user of the platform", e);
        }
    }
}
