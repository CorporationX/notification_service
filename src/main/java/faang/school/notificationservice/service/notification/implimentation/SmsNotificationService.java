package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.enums.PreferredContact;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService extends AbstractNotificationService{

    public SmsNotificationService() {
        super(PreferredContact.PHONE);
    }
}