package faang.school.notificationservice.config.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserChatIdUpdateDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UserServiceClient userServiceClient;

    @Value("${telegram.bot_username}")
    private String botUsername;

    public TelegramBot(@Value("${telegram.botToken}") String botToken, UserServiceClient userServiceClient) {
        super(botToken);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if (text.startsWith("/start")) {
                String[] parts = text.split(" ");
                if (parts.length > 1) {
                    try {
                        long userId = Long.parseLong(parts[1]);
                        linkUserWithChatId(userId, chatId);
                        sendMessage(chatId, "✅ Ты успешно привязан к боту!");
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "⚠ Ошибка: неверный формат команды.");
                    }
                } else {
                    sendMessage(chatId, "Привет! Отправь мне команду с идентификатором.");
                }
            }
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Retryable(retryFor = {FeignException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2))
    private void linkUserWithChatId(long userId, long chatId) {
        UserChatIdUpdateDto userChatIdUpdateDto = new UserChatIdUpdateDto();
        userChatIdUpdateDto.setId(userId);
        userChatIdUpdateDto.setChatId(chatId);

        userServiceClient.updateUserChat(userChatIdUpdateDto);
    }
}
