package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {

    UserDto.PreferredContact getPreferredContact();

    void sendNotification(UserDto user, String text);
}
