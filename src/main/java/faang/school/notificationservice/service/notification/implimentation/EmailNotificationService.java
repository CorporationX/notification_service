package faang.school.notificationservice.service.notification.implimentation;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailNotificationService implements NotificationService {

    private final MailProperties mailProperties;
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Override
    public void send(UserDto user, String message) {
        String subject = messageSource.getMessage("email.default.subject", null, user.getLocale());
        sendTextMessage(user.getEmail(), subject, message);
        log.info("Send notification to {} via {}: {}", user.getUsername(), getPreferredContact(), message);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.EMAIL;
    }

    private void sendTextMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getUsername());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send email notification!", e);
        }
    }
}