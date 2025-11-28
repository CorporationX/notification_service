package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Notification");
        email.setTo(user.getEmail());
        email.setText(message);

        mailSender.send(email);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
