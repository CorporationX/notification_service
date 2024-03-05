package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderMail;

    public void sendMail(String receiver, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }


    @Override
    public void send(UserDto user, String message) {
        sendMail(user.getEmail(), "Title", message);
    }
}