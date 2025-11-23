package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationException;
import faang.school.notificationservice.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;
    private final RetryTemplate retryTemplate;

    @Value("${spring.mail.from}")
    private String fromAddress;
    @Value("${spring.app.name}")
    private String appName;

    @Override
    public void send(UserDto user, String message) {
        validateParams(user, message);

        String userEmail = user.getEmail();

        try {
            retryTemplate.execute(context -> {
                sendEmail(userEmail, message);
                log.info("Email sent successfully to: {} (attempt {})",
                        userEmail, context.getRetryCount() + 1);
                return null;
            });
        } catch (MailSendException e) {
            throw new NotificationException("Email service temporarily unavailable for: %s".formatted(userEmail));
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private void sendEmail(String userEmail, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddress);
        mailMessage.setTo(userEmail);
        mailMessage.setSubject(appName);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    private void validateParams(UserDto user, String message) {
        List<String> errors = new ArrayList<>();

        if (fromAddress == null || fromAddress.isEmpty()) {
            errors.add("Sender email address is not configured");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            errors.add("User email is missing");
        }

        if (message == null || message.trim().isEmpty()) {
            errors.add("Message cannot be null or empty");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
