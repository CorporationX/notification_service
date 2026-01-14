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
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject("Notification");
        mail.setTo(user.getEmail());
        mail.setText(message);

        mailSender.send(mail);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
