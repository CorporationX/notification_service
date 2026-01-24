package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;

    @Override
    public void send(UserDto user, String message) {

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return null;
    }
}
