package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {
    private final MailSender mailSender;
    private final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    @Override
    public void send(UserNotificationDto userDto, String message) {
        simpleMailMessage.setTo(userDto.getEmail());
        simpleMailMessage.setSubject("Like is set to your post");
        simpleMailMessage.setText(message);

        this.mailSender.send(simpleMailMessage);
        log.info("post like notification");
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
