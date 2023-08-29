package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements NotificationService{

    @Override
    public void sendNotification(String message, UserDto userDto) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

}