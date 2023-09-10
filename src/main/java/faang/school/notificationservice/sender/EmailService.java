package faang.school.notificationservice.sender;

import faang.school.notificationservice.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.sender.email}")
    private String senderMail;

    public void sendMail(String receiver, String topic, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(receiver);
        message.setSubject(topic);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void send(UserDto user, String topic, String message) {
        sendMail(user.getEmail(), topic, message);
    }


    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}