package faang.school.notificationservice.service;

import faang.school.notificationservice.model.dto.UserDto;

public interface NotificationService {
    void send(UserDto user, String message);
    UserDto.PreferredContact getPreferredContact();
}
