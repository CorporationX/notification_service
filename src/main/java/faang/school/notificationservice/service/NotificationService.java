package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;

public interface NotificationService {

    void send(UserDto user, String message);

    PreferredContact getPreferredContact();
}
