package faang.school.notificationservice.event.data.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;

public class TestEmailNotificationService implements NotificationService {
    @Override
    public void send(UserDto user, String message) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
