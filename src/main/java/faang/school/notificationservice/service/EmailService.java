package faang.school.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final Environment env;

    public void sendMail(String receiver, String subject, String text) {
        String senderMail = env.getProperty("spring.mail.sender.email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}