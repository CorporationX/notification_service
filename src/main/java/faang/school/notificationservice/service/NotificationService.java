package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface NotificationService {
    void send(@Valid UserDto user, String message);
    PreferredContact getPreferredContact();
}
