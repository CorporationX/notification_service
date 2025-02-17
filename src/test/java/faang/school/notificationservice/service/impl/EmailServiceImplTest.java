package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.config.email.MailProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Spy
    JavaMailSender javaMailSender;

    @Spy
    MailProperties mailProperties;
    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void sendSimpleMessage() {
        String email = "test@mail.ru";
        String body = "testBody";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("staff@x.com");
        message.setTo(email);
        message.setSubject("");
        message.setText(body);

        Mockito.when(mailProperties.getFrom()).thenReturn("staff@x.com");
        emailService.sendSimpleMessage(email, "", body);
        Mockito.verify(javaMailSender, Mockito.times(1)).send(message);
    }
}