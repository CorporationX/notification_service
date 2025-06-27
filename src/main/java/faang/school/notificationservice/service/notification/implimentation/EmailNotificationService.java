package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationService extends AbstractNotificationService{

    public EmailNotificationService() {
        super(PreferredContact.EMAIL);
    }

    @Override
    public void send(UserDto user, String message) {
        log.info("Send notification to {} via {}: {}", user.getUsername(), getPreferredContact(), message);
    }
}