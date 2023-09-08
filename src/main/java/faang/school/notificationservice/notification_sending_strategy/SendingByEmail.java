package faang.school.notificationservice.notification_sending_strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendingByEmail implements SendingNotification {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.sender.email}")
    private String senderMail;

    @Override
    public void sending(String email, String title, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(email);
        message.setSubject(title);
        message.setText(messageText);
        javaMailSender.send(message);
    }
}
