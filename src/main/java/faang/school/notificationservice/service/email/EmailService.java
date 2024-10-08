package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender emailMailSender;
    @Value("${spring.mail.subject}")
    private String subject;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(sender);
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);
        emailMailSender.send(email);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
