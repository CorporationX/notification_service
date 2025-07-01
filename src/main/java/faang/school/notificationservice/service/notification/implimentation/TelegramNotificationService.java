package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TelegramNotificationService implements NotificationService {

    @Override
    public void send(UserDto user, String message) {
        log.info("Send notification to {} via {}: {}", user.getUsername(), getPreferredContact(), message);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}