package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {
    void send(UserDto userDto, String text);

    UserDto.PreferredContact getPreferredContact();
}