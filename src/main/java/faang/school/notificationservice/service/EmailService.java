package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService{
    private final JavaMailSender emailSender;

    @Override
    public void send(UserDto user, String message) {
        send(user.getEmail(), message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }

    private void send(
            String toEmail, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your@gmail.com");
        message.setTo(toEmail);
        message.setText(text);
        emailSender.send(message);
    }
}
