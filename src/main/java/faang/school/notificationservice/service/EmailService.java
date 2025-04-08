package faang.school.notificationservice.service;

import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.exception.InvalidUserIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.zip.DataFormatException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public String sendSimpleEmail(String to, String subject, String text, Long userId) {
        if(userId == null || userId <= 0) {
            throw new InvalidUserIdException("Передано неверное id пользователя");
        }
        log.info("Сообщение в сервисе");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        log.info("Сообщение отправлено");

        return "Successful";
    }

}
