package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TelegramNotificationService extends AbstractNotificationService {

    public TelegramNotificationService() {
        super(PreferredContact.TELEGRAM);
    }

    @Override
    public void send(UserDto user, String message) {
        log.info("Send notification to {} via {}: {}", user.getUsername(), getPreferredContact(), message);
    }
}