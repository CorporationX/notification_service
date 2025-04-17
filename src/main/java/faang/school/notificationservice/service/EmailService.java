package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.properties.EmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final EmailProperties emailProperties;

    @Override
    public void send(UserDto user, String message) {
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            log.warn("Trying to send an email to a user without an email: {}", user);
            return;
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Notice from CorporationX");
            mailMessage.setText(message);
            mailMessage.setFrom(emailProperties.getFrom());

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
