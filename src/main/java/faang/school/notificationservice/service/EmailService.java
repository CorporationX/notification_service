package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.sender.email}")
    private String senderMail;

    @Override
    public void send(UserDto user, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(senderMail);
        mail.setTo(user.getEmail());
        mail.setSubject("Notification from CorporationX");
        mail.setText(message);
        javaMailSender.send(mail);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}