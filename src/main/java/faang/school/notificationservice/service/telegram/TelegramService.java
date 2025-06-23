package faang.school.notificationservice.service.telegram;

import faang.school.notificationservice.dto.client.user_service.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelegramService implements NotificationService {
    @Override
    public void send(UserDto user, String message) {
        log.info("Telegram sent to user {} (phone: {}): {}", user.getUsername(), user.getPhone(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
