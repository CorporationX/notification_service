package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.PregerredContactNotification;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserEventDto;
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
    public void send(UserEventDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(message);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(SUBJECT);
        javaMailSender.send(mailMessage);
    }

    @Override
    public PregerredContactNotification getPreferredContact(UserEventDto dto) {
        return dto.getPreference();
    }
}
