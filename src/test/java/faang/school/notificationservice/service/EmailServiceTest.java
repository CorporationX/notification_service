package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private EmailService emailService;
    @Captor
    ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    void testSend() {
        String email = "user@example.com";
        UserDto user = new UserDto();
        user.setEmail(email);

        String text = "text";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText(text);

        mailSender.send(message);
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        List<String> recievers = Arrays.asList(capturedMessage.getTo());

        assertTrue(recievers.contains(email));
        assertEquals(text, capturedMessage.getText());
    }

    @Test
    void testGetPreferredContact() {
        UserDto.PreferredContact expected = UserDto.PreferredContact.EMAIL;
        assertEquals(expected, emailService.getPreferredContact());
    }
}