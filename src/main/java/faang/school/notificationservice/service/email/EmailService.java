package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.validator.EmailServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final EmailServiceValidator emailServiceValidator;
    @Value("${spring.mail.username}")
    private String emailFrom;
    private final String SUBJECT_MESSAGE = "Notification service";

    @Override
    public void send(UserDto user, String message) {
        emailServiceValidator.validationParam(user, message);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(SUBJECT_MESSAGE);
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setText(message);
        simpleMailMessage.setTo(user.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
