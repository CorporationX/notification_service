package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationService implements NotificationService{
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    @Override
    public void send(UserDto userDto, String messageText) {

    }
}
