package faang.school.notificationservice.service.email;

import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;

    @Setter
    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto user, String message) {
        String recipient = user.getEmail();

        String subject = "Hello, " + user.getUsername();
        String template = "Hello, " + user.getUsername() + "!\n\n"
                + message + "\n\n";

        try {
            sendEmail(recipient, subject, template);
        } catch (MessagingException e) {
            log.error("Error occurred while sending email: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void sendEmail(String recipient, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(emailFrom);

        mimeMessageHelper.setTo(recipient);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, false);

        log.info("Sending email to: {}", recipient);
        mailSender.send(mimeMessage);
    }
}
