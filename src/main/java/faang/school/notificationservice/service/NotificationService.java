package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserNotificationDto;

public interface NotificationService {

    void send(UserNotificationDto user, String message);

    UserDto.PreferredContact getPreferredContact();
}
