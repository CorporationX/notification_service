package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.handler.EmailSendingException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailSender;
    @Value("${spring.mail.subject}")
    private String emailSubject;

    @Override
    public CompletableFuture<Void> send(UserDto user, String message) {
        try {
            String email = user.getEmail();
            validateEmail(email);

            SimpleMailMessage messageToSend = new SimpleMailMessage();
            messageToSend.setTo(email);
            messageToSend.setFrom(emailSender);
            messageToSend.setSubject(emailSubject);
            messageToSend.setText(message);

            javaMailSender.send(messageToSend);

            log.info("Email sent to {}", email);
            return CompletableFuture.completedFuture(null);

        } catch (EmailSendingException e) {
            log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
            return CompletableFuture.failedFuture(e);
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}", user.getEmail(), e);
            return CompletableFuture.failedFuture(new EmailSendingException("Unexpected error while sending email", e));
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private void validateEmail(String email) {
        if (email == null) {
            log.error("Email address is null");
            throw new EmailSendingException("Email address cannot be null");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            log.error("Invalid email address format: {}", email);
            throw new EmailSendingException("Invalid email address: " + email);
        }
    }
}
