package faang.school.notificationservice.service.email;

import faang.school.notificationservice.config.notification.MailProperties;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationServiceHandler {
    final private MailProperties mailProperties;

    public SimpleMailMessage getSipmleMailMessage(UserDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());
        mailMessage.setTo(user.getEmail());
        mailMessage.setText(message);
        return mailMessage;
    }
}
