package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.sender.email}")
    private String senderMail;

    @Override
    public void send(UserNotificationDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(senderMail);
        mailMessage.setTo(user.getEmail());
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }

    @Override
    public UserNotificationDto.PreferredContact getPreferredContact() {
        return UserNotificationDto.PreferredContact.EMAIL;
    }
}