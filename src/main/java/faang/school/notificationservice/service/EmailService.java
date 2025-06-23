package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {
    private final JavaMailSender emailSender;

    @Value("${email.common-subject}")
    private String subject;

    @Override
    public void send(UserDto user, String message) {
        log.debug("Start sending email for user with id {} with message {}", user.getId(), message);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setText(message);
        mailMessage.setSubject(subject);
        mailMessage.setSentDate(new Date());
        emailSender.send(mailMessage);
        log.info("Email for user with id {} with message {} was sent", user.getId(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}