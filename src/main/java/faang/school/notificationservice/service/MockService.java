package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class MockService implements NotificationService{
    @Override
    public void send(UserDto user, String message) {
        System.err.println("message: " + message);
        System.err.println("for user: " + user.getUsername());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
