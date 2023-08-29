package faang.school.notificationservice.telegram;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ContactDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
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

@Component
@Slf4j
public class TelegramNotificationBot extends TelegramLongPollingBot implements NotificationService {
    @Value("${telegram.bot.name}")
    private String name;
    private final UserServiceClient userServiceClient;

    public TelegramNotificationBot(@Value("${telegram.bot.token}") String token, UserServiceClient userServiceClient) {
        super(token);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if ("/reg".equals(text)) {
            SendMessage registrationMessage = SendMessage.builder()
                    .text("Please provide your ID:")
                    .chatId(chatId)
                    .build();
            try {
                execute(registrationMessage);
            } catch (TelegramApiException e) {
                log.error("An error occurred while trying to send a message: {}", e.getMessage());
            }
        } else {
            processUserResponse(chatId, text);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
    private void sendMessage(long chat_id, String rowMessage) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chat_id)
                .text(rowMessage)
                .build();
        execute(message);
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void processUserResponse(long chatId, String response) {
        try {
            long userId = Long.parseLong(response);
            userServiceClient.sendTelegramId(userId, chatId);
        } catch (NumberFormatException | FeignException.FeignClientException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void send(UserDto user, String message) {
        var telegramContact = user.getContacts().stream().filter(contact -> contact.getType() == ContactDto.ContactType.TELEGRAM).findFirst();
        telegramContact.ifPresent(
                contact -> {
                    try {
                        sendMessage(Integer.parseInt(contact.getContact()), message);
                    } catch (TelegramApiException e) {
                        log.error("Error with sending notification to " + user, e);
                    }
                }
        );
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}

