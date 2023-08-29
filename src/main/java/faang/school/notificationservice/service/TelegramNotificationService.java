package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public class TelegramNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message, UserDto userDto) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
