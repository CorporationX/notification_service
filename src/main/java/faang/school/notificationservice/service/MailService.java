package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService implements NotificationService {
    public static final String SUBJECT = "Hello from CorporationX!";

    private final JavaMailSender javaMailSender;

    @Override
    public void send(UserNotificationDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(message);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(SUBJECT);
        javaMailSender.send(mailMessage);
    }

    @Override
    public UserNotificationDto.PreferredContact getPreferredContact() {
        return UserNotificationDto.PreferredContact.EMAIL;
    }
}
