package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    public final static String UNABLE_TO_SEND_NOTIFICATION = "Unable to send notification to mail. Mail address is incorrect for user: %s";

    @Value("${mail.subject}")
    private String subject;

    @Value("${mail.from}")
    private String from;

    private final JavaMailSender emailSender;

    @Override
    public void send(UserDto user, String text) {
        validateEmail(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getEmail().trim());
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private void validateEmail(UserDto user) {
        if (user.getEmail() == null || !EmailValidator.getInstance().isValid(user.getEmail())) {
            String message = String.format(UNABLE_TO_SEND_NOTIFICATION, user.getId());
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
