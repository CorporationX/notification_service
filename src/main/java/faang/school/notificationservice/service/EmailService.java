package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.NotificationService;
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

    private final JavaMailSender emailSender;
    @Value("${email.subject}")
    private String subject;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage templateMessage = new SimpleMailMessage();
        templateMessage.setSubject(subject);
        templateMessage.setFrom(user.getUsername());
        templateMessage.setTo(user.getEmail());
        templateMessage.setText(message);

        emailSender.send(templateMessage);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
