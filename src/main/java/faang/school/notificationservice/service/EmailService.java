package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    public static final String INVALID_EMAIL = "Cannot send email: user email is missing.";

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void send(UserDto user, String message) {
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            log.warn("Trying to send an email to a user without an email: {}", user);
            throw new InvalidEmailException(INVALID_EMAIL);
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Notice from CorporationX");
            mailMessage.setText(message);
            mailMessage.setFrom(from);

            javaMailSender.send(mailMessage);
            log.info("Email successfully sent : {}", email);
        } catch (MailException e) {
            log.error("Error when sending an e-mail to {}: {}", email, e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
