package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject("CorporationX");
        mailSender.send(simpleMailMessage);
        log.info("Email message sent successfully to " + user.getEmail());
    }
}
