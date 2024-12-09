package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserForNotificationDto;

public interface NotificationService {
    void send(UserForNotificationDto user, String message);

    PreferredContact getPreferredContact();

}
