package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.user.PreferredContact;
import faang.school.notificationservice.dto.user.UserForNotificationDto;

public interface NotificationService {
    void send(UserForNotificationDto user, String message);

    PreferredContact getPreferredContact();
}
