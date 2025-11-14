package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RetryTemplate retryTemplate;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromAddress", "noreply@test.com");
        ReflectionTestUtils.setField(emailService, "appName", "TestApp");
    }

    @Test
    void sendEmail_success() {
        UserDto user = new UserDto();
        user.setEmail("user@test.com");
        String message = "Hello";

        RetryTemplate template = new RetryTemplate();
        ReflectionTestUtils.setField(emailService, "retryTemplate", template);

        emailService.send(user, message);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("user@test.com", Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("noreply@test.com", sentMessage.getFrom());
        assertEquals("TestApp", sentMessage.getSubject());
        assertEquals(message, sentMessage.getText());
    }

    @Test
    void sendEmail_nullOrEmptyMessage_throwsException() {
        UserDto user = new UserDto();
        user.setEmail("user@test.com");

        assertThrows(IllegalArgumentException.class, () -> emailService.send(user, null));
        assertThrows(IllegalArgumentException.class, () -> emailService.send(user, "  "));
    }

    @Test
    void sendEmail_nullOrEmptyUserEmail_throwsException() {
        UserDto user = new UserDto();
        user.setEmail(null);

        assertThrows(IllegalArgumentException.class, () -> emailService.send(user, "Hello"));

        user.setEmail("");
        assertThrows(IllegalArgumentException.class, () -> emailService.send(user, "Hello"));
    }

    @Test
    void sendEmail_mailSendException_throwsNotificationException() {
        UserDto user = new UserDto();
        user.setEmail("user@test.com");

        doThrow(new MailSendException("Fail"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        RetryTemplate template = new RetryTemplate();
        ReflectionTestUtils.setField(emailService, "retryTemplate", template);

        NotificationException ex = assertThrows(NotificationException.class,
                () -> emailService.send(user, "Hello"));

        assertTrue(ex.getMessage().contains("user@test.com"));
    }

    @Test
    void getPreferredContact_returnsEmail() {
        assertEquals(UserDto.PreferredContact.EMAIL, emailService.getPreferredContact());
    }
}
