package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.entity.PreferredContact;

public class TelegramService implements NotificationService {
    @Override
    public void sendNotification(Long receiverId, String message) {

    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.TELEGRAM;
    }
}
