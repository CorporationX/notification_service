package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.NotificationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    private final String notificationServiceEmail;


    @Override
    public void send(@NotNull UserDto user, @NotBlank String message) {

        if (user.getEmail() == null){
            log.error(
                    "Failed to send notification to user {} due to missing email address",
                    user.getId()
            );
            return;
        }

        SimpleMailMessage mail = buildMailMessage(user, message);

        mailSender.send(mail);
    }

    private SimpleMailMessage buildMailMessage(UserDto user, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(notificationServiceEmail);
        mail.setTo(user.getEmail());
        mail.setText(message);

        return mail;
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
