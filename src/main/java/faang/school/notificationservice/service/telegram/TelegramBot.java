package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UserServiceClient userServiceClient;

    public TelegramBot(
            @Value("${telegram.bot-token}") String botToken,
            UserServiceClient userServiceClient) {
        super(botToken);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start")) {
                handleStartCommand(chatId, messageText);
            }
        }
    }

    private void handleStartCommand(Long chatId, String command) {
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            sendMessage(chatId, "To receive notifications, enter your user ID after the /start command. Example: /start 12345");
            return;
        }

        try {
            long userId = Long.parseLong(parts[1]);
            userServiceClient.updateUserTelegramChatId(userId, chatId);
            sendMessage(chatId, "✅ Your Telegram Chat ID has been saved. You will now receive notifications here!");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "❌ Invalid ID format. Please use a numeric ID.");
        } catch (Exception e) {
            sendMessage(chatId, "⚠️ An error occurred. Make sure the ID is correct and try again.");
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "NotificationBot";
    }
}