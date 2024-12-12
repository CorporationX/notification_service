package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    public final static String NOT_THE_PREFERRED_PLATFORM = "Telegram is not the preferred platform for sending notifications for user: %s";
    public final static String UNABLE_TO_SEND_NOTIFICATION = "Unable to send notification to mail. Mail address is incorrect for user: %s";

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Value("${mail.subject}")
    private String subject;

    @Value("${mail.from}")
    private String from;

    private final JavaMailSender emailSender;

    @Override
    public void send(UserDto user, String text) {
        validateUser(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private void validateUser(UserDto user) {
        if (!user.getPreference().equals(UserDto.PreferredContact.EMAIL)) {
            String massage = String.format(NOT_THE_PREFERRED_PLATFORM, user.getId());
            log.error(massage);
            throw new IllegalArgumentException(massage);
        }
        if (user.getEmail() == null || !validate(user.getEmail())) {
            String massage = String.format(UNABLE_TO_SEND_NOTIFICATION, user.getId());
            log.error(massage);
            throw new IllegalArgumentException(massage);
        }
    }

    private boolean validate(String emailString) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailString);
        return matcher.matches();
    }
}
