package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender javaMailSender;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto user, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("atraxlxxxl@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Notification from CorporationX");
        message.setText(text);
        javaMailSender.send(message);
        log.info("Message sent successfully");
    }
}
