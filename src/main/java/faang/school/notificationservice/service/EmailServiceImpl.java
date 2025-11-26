package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.EmailGatewayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements NotificationService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void send(UserDto user, String message) {
        try {
            SimpleMailMessage mailmessage = new SimpleMailMessage();
            mailmessage.setFrom(fromEmail);
            mailmessage.setTo(user.email());
            mailmessage.setSubject("Notification");
            mailmessage.setText(message);
            emailSender.send(mailmessage);
            log.info("Email sent to: {}", user.email());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", user.email(), e);
            throw new EmailGatewayException("Failed to send email to " + user.email() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
