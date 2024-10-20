package faang.school.notificationservice;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender emailSender;

    @Mock
    UserDto user;

    @BeforeEach
    void setUp() {
        when(user.getEmail()).thenReturn("test@gmail.com");
    }

    @Test
    void testSendEmail() {
        String message = "Message";

        emailService.send(user, message);

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));

        SimpleMailMessage emailMessageArgument = captureEmailMessageArgument();
        assertEquals("almeevamirhan294@gmail.com", emailMessageArgument.getFrom());
        assertEquals("test@gmail.com", emailMessageArgument.getTo()[0]);
        assertEquals(message, emailMessageArgument.getText());
    }

    private SimpleMailMessage captureEmailMessageArgument() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender).send(captor.capture());
        return captor.getValue();
    }

}
