package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {

    void send(Long id, String message);

    UserDto.PreferredContact getPreferredContact();
}
