package faang.school.notificationservice.service;

import faang.school.notificationservice.config.email.EmailSenderConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmailNotValidException;
import faang.school.notificationservice.exception.MessageSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSenderImpl mailSender;
    private final EmailSenderConfig senderConfig;

    @Override
    public void send(UserDto user, String message) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Not found: {}", user);
            throw new EmailNotValidException("Email address is empty");
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(user.getEmail());
        simpleMailMessage.setTo(senderConfig.username());
        simpleMailMessage.setSubject(user.getUsername());
        simpleMailMessage.setText(message);
        try {
            mailSender.send(simpleMailMessage);
            log.info("Email sent from: {}", user.getEmail());
        } catch (MailException e) {
            log.error("Failed to send email from: {}", user.getEmail());
            throw new MessageSendException(e.getMessage());
        }

    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
