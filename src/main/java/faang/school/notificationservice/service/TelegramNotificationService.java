package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class TelegramNotificationService implements NotificationService{
    @Override
    public void send(UserDto user, String message) {
        System.out.printf("\nTelegram notification sent to user %s. Message: %s",user.getUsername(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.TELEGRAM;
    }
}
