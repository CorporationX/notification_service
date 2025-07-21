package faang.school.notificationservice.client.telegram.bot;

import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.config.properties.TelegramProperties;
import faang.school.notificationservice.service.notification.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static faang.school.notificationservice.utils.ConstantSettings.INCORRECT_USER_ID;
import static faang.school.notificationservice.utils.ConstantSettings.NO_USER_ID;
import static faang.school.notificationservice.utils.ConstantSettings.START_STRING;
import static faang.school.notificationservice.utils.ConstantSettings.START_SYMBOLS_AMOUNT;
import static faang.school.notificationservice.utils.ConstantSettings.TELEGRAM_CONNECTED;

@Slf4j
@Component
public class CorpXTelegramBot extends TelegramLongPollingBot {
    private final TelegramProperties telegramProperties;
    private final TelegramUserService telegramUserService;
    private final UserContext userContext;

    public CorpXTelegramBot(TelegramProperties telegramProperties,
                            TelegramUserService telegramUserService,
                            UserContext userContext) {
        super(telegramProperties.getToken());
        this.telegramProperties = telegramProperties;
        this.telegramUserService = telegramUserService;
        this.userContext = userContext;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getUsername();
    }

    private void handleTextMessage(Message message) {
        log.info("CorpXTelegramBot handleTextMessage starts, message = {}", message);
        String text = message.getText();
        Long chatId = message.getChatId();

        if (text.startsWith(START_STRING)) {
            handleStartCommand(text, chatId);
        }
    }

    private void handleStartCommand(String text, Long chatId) {
        String payload = text.substring(START_SYMBOLS_AMOUNT);
        log.info("CorpXTelegramBot handleStartCommand starts, payload = {}", payload);
        if (payload.isBlank()) {
            log.error(NO_USER_ID);
            sendMessage(chatId, NO_USER_ID);
            return;
        }

        try {
            long userId = Long.parseLong(payload.trim());
            telegramUserService.saveUserChatId(userId, chatId);
            sendMessage(chatId, TELEGRAM_CONNECTED);
        } catch (NumberFormatException e) {
            log.error(INCORRECT_USER_ID);
            sendMessage(chatId, INCORRECT_USER_ID);
        }
    }

    public void sendMessage(Long chatId, String text) {
        log.info("CorpXTelegramBot sendMessage starts, chatId = {}, text = {}", chatId, text);
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException caught, {}", e.getMessage());
        }
    }

    public String generateStartLink() {
        long userId = userContext.getUserId();
        String startLink = String.format(telegramProperties.getLinkTemplate(), userId);
        log.info("Start link: {}", startLink);
        return startLink;
    }
}
