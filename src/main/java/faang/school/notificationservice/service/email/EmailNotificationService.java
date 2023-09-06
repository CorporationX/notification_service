package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import static faang.school.notificationservice.commonMessage.ErrorMessageForEmailService.EMAIL_ADDRESS_IS_NULL;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
    private final MailSender mailSender;

    @Override
    public void send(UserDto user, String message) {
        String userEmail = user.email();
        if (isNull(userEmail)) {
            throw new DataValidationException(EMAIL_ADDRESS_IS_NULL);
        }

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
