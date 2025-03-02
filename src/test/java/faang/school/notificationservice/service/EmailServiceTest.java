package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.SendNotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private UserServiceDto userDto;
    private String message;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(emailService, "sendFrom", "sender@example.com");
        ReflectionTestUtils.setField(emailService, "subject", "Test Subject");

        userDto = UserServiceDto.builder()
                .preference(UserServiceDto.PreferredContact.EMAIL)
                .email("user@example.com")
                .username("user")
                .build();

        message = "Test message";
    }

    @Test
    public void testSendEmailSuccess() {
        emailService.send(userDto, message);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailInvalidPreference() {
        userDto.setPreference(UserServiceDto.PreferredContact.SMS);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            emailService.send(userDto, message);
        });

        assertTrue(exception.getMessage().contains("User user didn't set email notification option"));
    }

    @Test
    public void testSendEmailInvalidEmail() {
       userDto.setEmail("invalid-email");

        Exception exception = assertThrows(SendNotificationException.class, () -> {
            emailService.send(userDto, message);
        });

        assertEquals("Email invalid-email is invalid", exception.getMessage());
    }

    @Test
    public void testSendEmailMailException() {
        doThrow(new MailSendException("Mail server error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        Exception exception = assertThrows(SendNotificationException.class, () -> {
            emailService.send(userDto, message);
        });

        String m = exception.getMessage();
        assertEquals("Email to user@example.com didn't send", exception.getMessage());
    }
}
