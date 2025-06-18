package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmailSendException;
import faang.school.notificationservice.exception.UserEmailMissingException;
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

    private final JavaMailSender mailSender;

    @Override
    public void send(UserDto user, String message) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new UserEmailMissingException(user.getId());
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Notification");
        mail.setText(message);

        try {
            mailSender.send(mail);
            log.info("Email successfully sent to {} (userId={})", user.getEmail(), user.getId());
        } catch (MailException e) {
            throw new EmailSendException(user.getEmail(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
