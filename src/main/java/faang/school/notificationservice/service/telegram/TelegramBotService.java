package faang.school.notificationservice.service.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final String telegramBotUsername;
    private final TelegramAccountService telegramAccountService;
    private final CommandProcessor commandProcessor;

    public TelegramBotService(String telegramBotToken,
                              String telegramBotUsername,
                              TelegramAccountService telegramAccountService, CommandProcessor commandProcessor) {
        super(telegramBotToken);
        this.telegramBotUsername = telegramBotUsername;
        this.telegramAccountService = telegramAccountService;
        this.commandProcessor = commandProcessor;
    }

    public void sendMessageTo(long userId, String message) {
        long chatId = telegramAccountService.getChatIdByUserId(userId);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        try {
            execute(sendMessage);
            log.error("Message = {} sent to user with id = {} and chatId = {}"
                    , message, userId, chatId);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException during try to execute SendMessage.class to user with id = {} and chatId = {}"
                    , userId, chatId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            try {
                execute(
                        commandProcessor.buildSendMessage(text, chatId)
                );
            } catch (UnsupportedOperationException e) {
                log.error("UnsupportedOperationException during building message for text = {} by user with chatId = {}",
                        text, chatId, e);
            } catch (TelegramApiException e) {
                log.error("TelegramApiException trying to send message with text = {} to user with chatId = {}",
                        text, chatId, e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }
}
