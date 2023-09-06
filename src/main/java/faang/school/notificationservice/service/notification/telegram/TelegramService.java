package faang.school.notificationservice.service.notification.telegram;

import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class TelegramService implements NotificationService {

    @Override
    public void sendNotification(Long id, String message) {

    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}
