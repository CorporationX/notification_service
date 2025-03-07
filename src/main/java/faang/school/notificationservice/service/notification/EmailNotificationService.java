package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.user.UserDto;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;
    private final Counter emailFailedCounter;
    private final Counter emailSuccessCounter;

    @Value("${spring.mail.from}")
    private String sender;

    public EmailNotificationService(JavaMailSender mailSender, MeterRegistry meterRegistry) {
        this.mailSender = mailSender;
        this.emailFailedCounter = meterRegistry.counter("email_failed");
        this.emailSuccessCounter = meterRegistry.counter("email_success");
    }

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Notification-service test");
        mailMessage.setText(message);
        try {
            mailSender.send(mailMessage);
            emailSuccessCounter.increment();
            log.info("Message {} sent to email {} success", message, user.getEmail());
        } catch (MailSendException ex) {
            log.error("Failed send message {} to email {}", message, user.getEmail());
            emailFailedCounter.increment();
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}