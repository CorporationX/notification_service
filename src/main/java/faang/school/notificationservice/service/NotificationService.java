package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserServiceDto;

public interface NotificationService {

    void send(UserServiceDto user, String message);

    UserServiceDto.PreferredContact getPreferredContact();
}
