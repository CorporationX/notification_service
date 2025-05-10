package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

import java.util.List;

public interface NotificationService {

    void send(UserDto user, String message);

    UserDto.PreferredContact getPreferredContact();
}
