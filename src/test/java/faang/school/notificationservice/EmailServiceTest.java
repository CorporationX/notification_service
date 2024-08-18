package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private UserDto user;

    @Test
    public void testSendEmail() {
        String message = "Hello World";
        Mockito.when(user.getEmail()).thenReturn("test@example.com");

        emailService.send(user, message);

        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mailSender).send(mailCaptor.capture());
        SimpleMailMessage mailMessage = mailCaptor.getValue();
        assertEquals("test@example.com", mailMessage.getTo()[0]);
        assertEquals("Notification", mailMessage.getSubject());
        assertEquals(message, mailMessage.getText());

    }
}
