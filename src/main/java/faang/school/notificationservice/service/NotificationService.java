package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {

    void sendNotification(String message, UserDto userDto);

    UserDto.PreferredContact getPreferredContact();
}
