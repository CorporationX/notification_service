package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {

    private final JavaMailSender emailSender;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    public void send(UserDto userDto, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userDto.getEmail());
        message.setSubject("Corporation-X notification");
        message.setText(text);
        log.info("Sending email with text: {} to: {}", text, userDto.getEmail());
        emailSender.send(message);
        log.info("Email sent successfully");
    }
}