package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {

    void send(String contactAddress, String subject, String message);

    UserDto.PreferredContact getPreferredContact();
}
