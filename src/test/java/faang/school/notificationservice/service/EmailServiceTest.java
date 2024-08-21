package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
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
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSend() {
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        String message = "Test message";

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.send(user, message);

        verify(mailSender).send(captor.capture());
        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("test@example.com", sentMessage.getTo()[0]);
        assertEquals(message, sentMessage.getText());
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = emailService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.EMAIL, preferredContact);
    }
}
