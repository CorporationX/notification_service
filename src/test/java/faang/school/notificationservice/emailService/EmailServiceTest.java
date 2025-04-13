package faang.school.notificationservice.emailService;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testShouldSendEmailSuccessfully() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@gmail.com");

        emailService.send(userDto, "Test message");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("test@gmail.com", sentMessage.getTo()[0]);
        assertEquals("Test message", sentMessage.getText());
    }

    @Test
    public void testShouldNotSendEmailIfUserEmailIsNull() {
        UserDto userDto = new UserDto();
        emailService.send(userDto, "Test message");

        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }
}
