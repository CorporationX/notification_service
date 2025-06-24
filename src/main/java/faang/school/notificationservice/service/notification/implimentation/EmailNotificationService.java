package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.enums.PreferredContact;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService extends AbstractNotificationService{

    public EmailNotificationService() {
        super(PreferredContact.EMAIL);
    }
}