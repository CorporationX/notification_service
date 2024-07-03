package faang.school.notificationservice.service;

import faang.school.notificationservice.config.notification.EmailProperties;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final EmailProperties emailProperties;
    private static final String DEFAULT_SUBJECT = "CorporationX";

    @Override
    public void send(UserDto user, String message) {
        if (user.getEmail().isBlank()) {
            log.info("No such email user");
            return;
        }

        if (message.isBlank()) {
            log.info("No message to send");
            return;
        }

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailProperties.getUsername());
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject(DEFAULT_SUBJECT);
        simpleMailMessage.setText(message);

        try {
            javaMailSender.send(simpleMailMessage);
            log.info("Mail was send");
        } catch (MailException ex) {
            log.error("Mail not send. Error {}", ex.getMessage());
            throw new RuntimeException("Error send mail");
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
