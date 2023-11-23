package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.sender_email}")
    private final String senderEMail;
    @Value("${spring.mail.default_subject}")
    private final String subject;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void sendNotification(String message, UserDto userDto) {
        log.info("Email message send to email address: {}", userDto.getEmail());
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(senderEMail);
        mailMessage.setTo(userDto.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
}
