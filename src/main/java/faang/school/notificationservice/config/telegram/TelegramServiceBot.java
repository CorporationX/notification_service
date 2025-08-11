package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.exception.TelegramMessageSendException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramServiceBot extends TelegramLongPollingBot {

    private final TelegramBotProperties telegramBotProperties;

    public TelegramServiceBot(TelegramBotProperties telegramBotProperties) {
        super(telegramBotProperties.token());
        this.telegramBotProperties = telegramBotProperties;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                log.info("Received message: {} from chat: {}", messageText, chatId);
            }
        } catch (Exception e) {
            log.error("Error processing update: {}", update, e);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperties.username();
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        
        try {
            execute(message);
            log.info("Message sent to chat: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat: {}", chatId, e);
            throw new TelegramMessageSendException("Failed to send telegram message", e);
        }
    }
}
