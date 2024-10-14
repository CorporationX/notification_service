package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender sender;
    private final EmailNotificationServiceHandler notificationServiceHandler;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage emailMessage = notificationServiceHandler.getSipmleMailMessage(user, message);
        sender.send(emailMessage);
        log.info("Sent message to: {}, from: {}", user.getUsername(), user.getEmail());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
