package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.default-subject}")
    private String subject;

    @Override
    public void send(UserDto user, String message) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            String fullMessage = addGreetingsAndSignature(user, message);
            helper.setText(fullMessage, true);
            emailSender.send(mimeMessage);
            log.info("EmailNotificationService: successfully sent email to user {}",
                    user.getUsername());
        } catch (MessagingException e) {
            log.error("EmailNotificationService: Failed to send email notification to user {}",
                    user.getUsername(), e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private String addGreetingsAndSignature(UserDto user, String message) {
        return String.format("<p> Dear %s,</p>" +
                        "<p>%s</p>" +
                        "<br>" +
                        "<p>Best regards,</p>" +
                        "<p><b>Hydra team</b></p>",
                user.getUsername(), message);
    }
}
