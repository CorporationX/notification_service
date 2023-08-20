package faang.school.notificationservice.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.bot.BotConfig;
import faang.school.notificationservice.dto.ExtendedContactDto;
import faang.school.notificationservice.dto.TgContactDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;

@Slf4j
public class NotificationTelegramBot extends TelegramLongPollingBot {
    private BotConfig config;
    private TelegramServiceImpl telegramService;
    private UserServiceClient userServiceClient;

    public NotificationTelegramBot(String botToken, BotConfig config) {
        super(botToken);
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        String userName = null;
        String receivedMessage;

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getFirstName();
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, memberName, phoneNumber);
            }

//        } else if (update.hasCallbackQuery()) {
//            chatId = update.getCallbackQuery().getMessage().getChatId();
//            userId = update.getCallbackQuery().getFrom().getId();
//            userName = update.getCallbackQuery().getFrom().getFirstName();
//            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
//            receivedMessage = update.getCallbackQuery().getData();
//
//            botAnswerUtils(receivedMessage, chatId, userName, phoneNumber);
//        }
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName, String phoneNumber) {
        Long userId = userServiceClient.findUserIdByPhoneNumber(phoneNumber);
        switch (receivedMessage) {
            case "/start":
                startBot(chatId, userName);
                break;
            case "/registration":
                initRegistration(userId, chatId, userName, phoneNumber);
                break;
            default:
                break;
                log.info("Unexpected message");

        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + userName + "! Send you contact to sign up in Telegram bot.");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void initRegistration(Long userId, long chatId, String phone, String userName) {
        UserDto user = userServiceClient.getUser(userId);
        ExtendedContactDto tgContact = userServiceClient.getUserContact(user.getId());

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if (tgContact.getPhone().equals(phone)
                && tgContact.getTgChatId().equals(chatId)) {
            message.setText("Registration is successful, " + userName + "! You can get telegram-notifications now. Thank you!");
        } else {
            message.setText("Telegram account is not correct, " + userName + "! Please, use you actual account for registration!");

            try {
                execute(message);
                log.info("Reply sent");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }
}
