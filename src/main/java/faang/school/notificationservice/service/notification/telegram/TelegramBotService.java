package faang.school.notificationservice.service.notification.telegram;

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
    private final CommandProcessor commandProcessor;

    public TelegramBotService(String telegramBotToken,
                              String telegramBotUsername,
                              CommandProcessor commandProcessor) {
        super(telegramBotToken);
        this.telegramBotUsername = telegramBotUsername;
        this.commandProcessor = commandProcessor;
    }

    public void sendMessageTo(long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        try {
            execute(sendMessage);
            log.info("Message = {} sent to chat with id = {}",
                    message, chatId);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException during try to execute SendMessage.class to chat with id = {}",
                    chatId, e);
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
