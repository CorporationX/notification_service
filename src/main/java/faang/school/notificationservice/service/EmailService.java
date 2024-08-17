package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage messageMail = new SimpleMailMessage();
        messageMail.setFrom(from);
        messageMail.setTo(user.getEmail());
        messageMail.setText(message);
        messageMail.setSubject("Notification");

        try {
            mailSender.send(messageMail);
            log.info("Message sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send message to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send message");
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
