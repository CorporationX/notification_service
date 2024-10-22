package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender mailService;

    @Override
    public void send(UserDto user, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Hello, " + user.getUsername());
        message.setText(text);
        mailService.send(message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}
