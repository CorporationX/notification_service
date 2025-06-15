package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.config.TelegramConfig;
import faang.school.notificationservice.exception.TelegramNotificationError;
import faang.school.notificationservice.service.TelegramService;
import faang.school.notificationservice.service.telegram.command.CommandContainer;
import faang.school.notificationservice.service.telegram.command.TelegramCommand;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String REGISTER_USER_IN_APP = "register.hello";
    public static final String REGISTER_PHONE = "register.phone";
    public static final String COMMAND_LIST = "command.list";

    private final TelegramConfig telegramConfig;
    private final CommandContainer commands;
    private final MessageSource messageSource;
    private final Locale locale;

    private final Map<String, String> messages = new HashMap<>();

    private TelegramService telegramService;

    public TelegramBot(
        TelegramConfig telegramConfig,
        List<TelegramCommand> commandList,
        MessageSource messageSource, Locale locale
    ) {
        super(telegramConfig.getBotToken());
        this.telegramConfig = telegramConfig;
        this.commands = new CommandContainer(this, commandList);
        this.messageSource = messageSource;
        this.locale = locale;
        log.debug("command list: {}", commands);
    }

    @PostConstruct
    public void registerCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        commands.getCommands().forEach((key, command) ->
            botCommands.add(new BotCommand(key, command.getDescription()))
        );

        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(botCommands);
        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            log.error("My command registration error. {}", e.getMessage(), e);
            throw new TelegramNotificationError("My command registration error");
        }

        initMessages();
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }

    @Override
    public synchronized void onUpdateReceived(Update update) {
        log.debug("onUpdateReceived; update: {}", update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            TelegramCommand command = commands.getCommand(message.getText());
            command.execute(update);
        } else if (update.hasMessage() && update.getMessage().hasContact()) {
            // handleProcessContact(update.getMessage().getContact(), update.getMessage().getChatId());
            handleProcessContact(update);
        } else {
            log.debug("message is empty");
        }
    }

    public void sendContactRequest(Long chatId) {
        try {
            ReplyKeyboardMarkup keyboardMarkup = createContactRequestKeyboard();
            SendMessage requestMsg = new SendMessage();
            requestMsg.setChatId(chatId);
            requestMsg.setText(messages.get(REGISTER_USER_IN_APP));
            requestMsg.setReplyMarkup(keyboardMarkup);
            execute(requestMsg);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendCommandList(Long chatId) {
        StringBuilder sb = new StringBuilder(messages.get(COMMAND_LIST));
        sb.append('\n');
        commands.getCommands().forEach((key, value) -> {
            sb.append(key).append(" - ");
            sb.append(value.getDescription()).append('\n');
        });
        sendMessage(chatId, sb.toString());
    }

    public void unregisterChatId(Long chatId) {
        telegramService.unregisterUser(chatId);
    }

    public void setService(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId.toString(), text);
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .build();
        try {
            execute(message); // Отправляем ответ пользователю
        } catch (TelegramApiException e) {
            log.error("Send message error. {}", e.getMessage(), e);
            throw new TelegramNotificationError("Send message error");
        }
    }

    private ReplyKeyboardMarkup createContactRequestKeyboard() {
        log.debug("create contact request keyboard");
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText(messages.get(REGISTER_PHONE));
        keyboardButton.setRequestContact(true);
        KeyboardRow row = new KeyboardRow();
        row.add(keyboardButton);
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setKeyboard(rows);

        return replyKeyboardMarkup;
    }

    private void handleProcessContact(Update update) {
        Contact contact = update.getMessage().getContact();
        if (update.getMessage().getFrom().getId().equals(contact.getUserId())) {
            log.info("Request to register the user's phone in the system. chatId: {}, phone: {}",
                update.getMessage().getChatId(), contact.getPhoneNumber()
            );
            telegramService.registerUser(update.getMessage().getChatId(), contact.getPhoneNumber());
        } else {
            log.error("Registration of someone else's phone is not possible. fromUserId: {}, contact.userId: {}",
                update.getMessage().getFrom().getId(), contact.getUserId()
            );
        }
    }

    // Убираем клавиатуру после получения контакта
    public void removeKeyboardAfterReceivingContact(Long chatId, String messageText) {
        log.debug("remove keyboard after receiving contact. chatId: {}", chatId);
        SendMessage message = SendMessage.builder()
            .chatId(chatId)
            .text(messageText)
            .build();

        // Создаем пустую клавиатуру для удаления старой
        ReplyKeyboardRemove removeKeyboard = new ReplyKeyboardRemove();
        removeKeyboard.setRemoveKeyboard(true);

        message.setReplyMarkup(removeKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Delete keyboard error. {}", e.getMessage(), e);
            throw new TelegramNotificationError("Delete keyboard error");
        }
    }

    private String getMessage(String messageId) {
        return messageSource.getMessage(messageId, null, locale);
    }

    private void initMessages() {
        messages.put(REGISTER_USER_IN_APP, getMessage(REGISTER_USER_IN_APP));
        messages.put(COMMAND_LIST, getMessage(COMMAND_LIST));
        messages.put(REGISTER_PHONE, getMessage(REGISTER_PHONE));
        log.debug("messages: {}", messages);
    }
}
