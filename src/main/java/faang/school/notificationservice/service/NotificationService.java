package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.PregerredContactNotification;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserEventDto;

public interface NotificationService {

    void send(UserEventDto user, String message);

    PregerredContactNotification getPreferredContact(UserEventDto dto);
}
