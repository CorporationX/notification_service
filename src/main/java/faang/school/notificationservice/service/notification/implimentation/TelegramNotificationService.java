package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.enums.PreferredContact;
import org.springframework.stereotype.Service;

@Service
public class TelegramNotificationService extends AbstractNotificationService {

    public TelegramNotificationService() {
        super(PreferredContact.TELEGRAM);
    }
}