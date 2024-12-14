package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.notification.TelegramConfig;

import faang.school.notificationservice.telegram.components.Buttons;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static faang.school.notificationservice.telegram.components.BotCommands.HELP_TEXT;

@Slf4j
@Component
public class TelegramBotService extends TelegramLongPollingBot {
    final TelegramConfig config;
    private final UserServiceClient userServiceClient;

    public TelegramBotService(TelegramConfig config, UserServiceClient userServiceClient) {
        this.config = config;
        this.userServiceClient = userServiceClient;

        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("/start", "get a welcome message"));
        botCommands.add(new BotCommand("/help", "displays information"));
        botCommands.add(new BotCommand("/log_in", "authorization"));

        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
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
        long chatId;
        long userId;
        String userName = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                String receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }

            if (update.getMessage().hasContact()) {
                Contact contact = update.getMessage().getContact();
                String phoneNumber = contact.getPhoneNumber();

                log.info("The phone number was received from the user {}: {}", userId, phoneNumber);

                authorizeUser(chatId, phoneNumber);
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            String receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, userName);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        switch (receivedMessage) {
            case "/start":
                startBot(chatId, userName);
                break;
            case "/help":
                sendMessageText(chatId, HELP_TEXT);
                break;
            case "/log_in":
                requestContact(chatId);
                break;
            default:
                sendMessageText(chatId, "The command is not recognized. Try /help.");
                break;
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hi, " + userName + "! I'm Corporation X bot.");
        message.setReplyMarkup(Buttons.secondInlineMarkup());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendMessageText(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void requestContact(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        KeyboardButton contactButton = new KeyboardButton();
        contactButton.setText("Share a phone number");
        contactButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Please send your phone number:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error when requesting a contact: " + e.getMessage());
        }
    }

    private void authorizeUser(long chatId, String phoneNumber) {
        Long userId = userServiceClient.findUserByPhone(phoneNumber);

        if (userId != null) {
            sendMessageText(chatId, "Authorization was successful, welcome!");

        } else {
            sendMessageText(chatId, "Authorization failed. Are you sure you've registered?");
        }
    }
}
