package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.entity.PreferredContact;

public interface NotificationService {

    void sendNotification(String message);

    PreferredContact getPreferredContact();
}
