package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import jakarta.mail.MessagingException;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private UserDto user;

    @Test
    public void send_sendsEmail_logsMessages() throws MessagingException {
        Mockito.when(user.getEmail()).thenReturn("test@example.com");
        String message = "This is a test message";

        emailService.send(user, message);

        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(mailCaptor.capture());
        SimpleMailMessage capturedMail = mailCaptor.getValue();
        assertEquals("test@example.com", capturedMail.getTo()[0]);
        assertEquals("Notification", capturedMail.getSubject());
        assertEquals(message, capturedMail.getText());
    }
}