package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService implements NotificationService {
    @Autowired
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom(username);
        emailMessage.setTo(user.getEmail());
        emailMessage.setSubject("CorporationXNotification");
        emailMessage.setText(message);
        emailSender.send(emailMessage);
        log.info("Message send to user: {}", user.getId());
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}