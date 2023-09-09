package faang.school.notificationservice.service;

import faang.school.notificationservice.sender.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;
    @Value("${spring.mail.sender.email}")
    private String senderMail;

    @Test
    void testSendMail() {
        String recipient = "test@mail.com";
        String subject = "subject";
        String text = "text";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(text);

        emailService.sendMail(recipient, subject, text);

        verify(mailSender, times(1)).send(message);
    }
}