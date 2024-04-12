package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;

public class TelegramService implements NotificationService {

    @Override
    public void send(UserDto user, String message) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }
}
