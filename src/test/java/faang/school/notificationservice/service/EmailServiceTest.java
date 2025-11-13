package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.EmailGatewayException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    private static final String USER_EMAIL = "test@gmail.com";
    private static final String MESSAGE_TEXT = "Test message";

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void send_WithValidDataSendsEmailSuccessfully() {
        UserDto user = createUser();
        emailService.send(user, MESSAGE_TEXT);
        verify(emailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void send_WhenEmailSendingFailsThrowsEmailGatewayException() {
        UserDto user = createUser();
        doThrow(new RuntimeException("SMTP error"))
                .when(emailSender).send(any(SimpleMailMessage.class));

        assertThrows(EmailGatewayException.class, () -> emailService.send(user, MESSAGE_TEXT));

        verify(emailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void getPreferredContact_ReturnsEmail() {
        UserDto.PreferredContact result = emailService.getPreferredContact();

        assertEquals(UserDto.PreferredContact.EMAIL, result);
    }

    private UserDto createUser() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail(USER_EMAIL);
        return user;
    }
}
