package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.validation.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender emailSender;
    private final EmailValidator emailValidator;

    @Override
    public void send(UserDto user, String message) {
        emailValidator.validateForEmail(user);

        log.info("Message send to user: {}, to email^ {}", user.getId(), user.getEmail());
        SimpleMailMessage emailMessage = createMessage(user.getEmail(), user.getUsername(), message);
        emailSender.send(emailMessage);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private SimpleMailMessage createMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        return message;
    }
}
