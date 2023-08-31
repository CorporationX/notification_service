package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
    private final MailSender mailSender;
    private final EmailValidator emailValidator;

    @Override
    public void send(UserDto user, String message) {
        String userEmail = user.email();
        emailValidator.checkEmail(userEmail);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userEmail);
        simpleMailMessage.setSubject(message);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.EMAIL;
    }
}
