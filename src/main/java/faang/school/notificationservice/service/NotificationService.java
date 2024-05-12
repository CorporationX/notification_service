package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;

public interface NotificationService {

    void send(UserDto user, String message);
    PreferredContact getPreferredContact();
}
