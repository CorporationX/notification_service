package faang.school.notificationservice.service.notification;


import faang.school.notificationservice.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements NotificationService {

    @Override
    public void send(UserDto user, String message) {
        System.out.println("Sending email to " + user.getEmail() + ": " + message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}