package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.RecommendationEmailTemplateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final RecommendationEmailTemplateBuilder recommendationEmailTemplateBuilder;

    @Async
    @Override
    public void send(UserDto user) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("uralostrov6@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(recommendationEmailTemplateBuilder.buildSubject(user));
        mailMessage.setText(recommendationEmailTemplateBuilder.buildMessage(user, Locale.ENGLISH));

        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.error("Ошибка при отправке письма: {}", e.getMessage(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.EMAIL;
    }
}