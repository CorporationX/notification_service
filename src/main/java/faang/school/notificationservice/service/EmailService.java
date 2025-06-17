package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    @Autowired
    private final JavaMailSender emailSender;

    @Value("spring.mail.username")
    private String emailAddress;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailAddress);
        msg.setTo(user.getEmail());
        msg.setSubject("Notification");
        msg.setText(message);
        emailSender.send(msg);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
