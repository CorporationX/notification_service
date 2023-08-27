package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {

    UserDto.PreferredContact getPreferredContact();

    void sendNotification(String messageText, UserDto userDto);
}
