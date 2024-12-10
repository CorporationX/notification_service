package faang.school.notificationservice.service;

import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import jakarta.validation.Valid;

public interface NotificationService {
    void send(@Valid UserContactsDto user, String message);

    NotificationChannel getPreferredContact();
}
