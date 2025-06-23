package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.client.user_service.UserDto;

public interface NotificationService {

    void send(UserDto user, String message);

    UserDto.PreferredContact getPreferredContact();
}
