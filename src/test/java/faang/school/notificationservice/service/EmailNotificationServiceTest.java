package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserProfileDto;
import faang.school.notificationservice.service.email.EmailNotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationService emailService;

    @Test
    void testSendEmail() {
        UserProfileDto user = new UserProfileDto();
        user.setId(1L);
        user.setEmail("user@example.com");
        String message = "Test message";

        emailService.send(user, message);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("user@example.com", sentMessage.getTo()[0]);
        assertEquals("New Notification", sentMessage.getSubject());
        assertEquals("Test message", sentMessage.getText());
    }
}