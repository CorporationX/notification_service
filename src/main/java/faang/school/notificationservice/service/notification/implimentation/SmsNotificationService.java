package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsNotificationService extends AbstractNotificationService{

    public SmsNotificationService() {
        super(PreferredContact.PHONE);
    }

    @Override
    public void send(UserDto user, String message) {
        log.info("Send notification to {} via {}: {}", user.getUsername(), getPreferredContact(), message);
    }
}