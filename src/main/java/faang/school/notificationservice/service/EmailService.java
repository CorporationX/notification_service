package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

public class EmailService implements NotificationService{
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto userDto, String messageText) {

    }
}
