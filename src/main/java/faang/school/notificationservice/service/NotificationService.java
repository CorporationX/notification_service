package faang.school.notificationservice.service;

import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.enums.PreferredContact;

public interface NotificationService {
    void send(UserDto user, String text);
    PreferredContact getPreferredContact();
}
