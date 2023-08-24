package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public class TelegramNotificationService implements NotificationService{
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }

    @Override
    public void send(UserDto userDto, String messageText) {

    }
}
