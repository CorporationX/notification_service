package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {
    private static final String EMAIL_SUBJECT_CODE = "email.subject";
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto user, String text) {
        send(user, text, EMAIL_SUBJECT_CODE, Locale.ENGLISH);
    }

    public void send(UserDto user, String text, String subjectCode, Locale locale) {
        try {
            String subject = messageSource.getMessage(subjectCode, null, locale);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send email to user: {}", user.getEmail(), e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while sending email to user: {}", user.getEmail(), e);
        }
    }
}