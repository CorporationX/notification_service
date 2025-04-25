package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

import java.util.List;

public interface NotificationService {

    void send(UserDto user, String message);

    void sendGroup(List<UserDto> users, String message);

    UserDto.PreferredContact getPreferredContact();
}
