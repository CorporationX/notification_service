package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.UserProfileDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class TelegramNotificationService implements NotificationService {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public TelegramNotificationService(
            RestTemplate restTemplate,
            @Value("${telegram.bot-token}") String botToken) {
        this.restTemplate = restTemplate;
        this.apiUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";
    }

    @Override
    public void send(UserProfileDto user, String message) {
        if (user.getTelegramChatId() == null) {
            log.warn("Cannot send Telegram notification: chat ID not found for user {}", user.getId());
            return;
        }

        restTemplate.postForObject(
                apiUrl,
                new TelegramMessage(user.getTelegramChatId(), message),
                String.class
        );
    }

    @Override
    public UserProfileDto.PreferredContact getPreferredContact() {
        return UserProfileDto.PreferredContact.TELEGRAM;
    }

    @Override
    public boolean supports(UserProfileDto.PreferredContact preferredContact) {
        return getPreferredContact() == preferredContact;
    }

    protected Object createMessageRequest(Long chatId, String text) {
        return new TelegramMessage(chatId, text);
    }
    private record TelegramMessage(Long chat_id, String text) {}
}