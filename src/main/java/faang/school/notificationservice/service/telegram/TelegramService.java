package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelegramService implements NotificationService {
    @Override
    public void send(UserClientResponseDto user, String message) {
        log.info("Telegram sent to user {} (phone: {}): {}", user.getUsername(), user.getPhone(), message);
    }

    @Override
    public UserClientResponseDto.PreferredContact getPreferredContact() {
        return UserClientResponseDto.PreferredContact.TELEGRAM;
    }
}
