package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public class SmsService implements NotificationService{
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    @Override
    public void send(UserDto userDto, String messageText) {

    }
}
