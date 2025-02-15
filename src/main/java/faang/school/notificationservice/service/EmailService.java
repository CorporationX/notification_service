package faang.school.notificationservice.service;

import faang.school.notificationservice.config.email.EmailProperties;
import faang.school.notificationservice.dto.UserDto;
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

    private final EmailProperties emailProperties;
    private final JavaMailSender emailSender;

    @Override
    public void send(UserDto user, String message) {
        user.setPreference(getPreferredContact());

        try {
            var mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailProperties.getUsername());
            mailMessage.setTo(user.getEmail());
            mailMessage.setText(message);
            emailSender.send(mailMessage);
        } catch (MailException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
