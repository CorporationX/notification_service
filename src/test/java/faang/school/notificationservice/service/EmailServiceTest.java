package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    private EmailService service;
    private String subject = "FAANG school";
    private String senderEMail = "noreply@faang-school.com";
    private UserDto userDto = UserDto.builder().email("user@gmail.com").build();

    @BeforeEach
    public void setUp() {
        service = new EmailService(javaMailSender, senderEMail, subject);
    }

    @Test
    public void testSend() {
        String messageText = "";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEMail);
        message.setTo(userDto.getEmail());
        message.setSubject(subject);
        message.setText(messageText);

        service.sendNotification(messageText, userDto);

        verify(javaMailSender).send(message);
    }
}