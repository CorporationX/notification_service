package faang.school.notificationservice.service;

import faang.school.notificationservice.exception.CustomTelegramException;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class CustomTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;

    @Autowired
    public CustomTelegramBot(Dotenv dotenv) {
        this.botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
        this.botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            log.info("Received message from chatId {}: {}", chatId, messageText);
            handleIncomingMessage(messageText, chatId);
        }
    }

    // https://t.me/bot_user_name?start={userId}
    private void handleIncomingMessage(String command, long chatId) {
        if (command.startsWith("/start")) {
            String[] commandParts = command.split(" ");
            if (commandParts.length > 1) {
                processUserBinding(commandParts[1], chatId);
            } else {
                sendMessage(chatId, "Please provide your userId after the /start command.");
            }
        }
    }

    private void processUserBinding(String userId, long chatId) {
        bindUserToTelegram(userId, chatId);
        sendMessage(chatId, "Your userId: " + userId + " has been successfully linked to your Telegram!");
    }

    public void sendMessage(long chatId, String text) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);
            execute(message);
            log.info("Sent message to chat {}: {}", chatId, text);
        } catch (TelegramApiException e) {
            log.error("Error sending message to chat {}: {}", chatId, e.getMessage());
            throw new CustomTelegramException("Failed to send message", e);
        }
    }

    private void bindUserToTelegram(String userId, long chatId) {
        log.info("Linking userId: {} with Telegram chatId: {}", userId, chatId);
    }
}