package faang.school.notificationservice.service;

import faang.school.notificationservice.data.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import jakarta.validation.Valid;

public interface NotificationService {
    void send(@Valid UserDto user, String message);
    PreferredContact getPreferredContact();
}
