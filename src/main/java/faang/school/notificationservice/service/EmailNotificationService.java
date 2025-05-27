package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements NotificationService{
    @Override
    public void send(UserDto user, String message) {
        System.out.printf("\nEmail notification sent to user %s. Message: %s",user.getUsername(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
