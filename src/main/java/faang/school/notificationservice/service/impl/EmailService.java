package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Override
    public void send(UserDto user, String message) {
        log.info("Trying to send email message: {} to user: {}", message, user.getId());
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom(emailAddress);
        emailMessage.setTo(user.getEmail());
        emailMessage.setText(message);
        emailSender.send(emailMessage);
        log.info("Message: {} was successfully sent to user: {}", message, user.getId());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
