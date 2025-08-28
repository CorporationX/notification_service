package faang.school.notificationservice.service;

import faang.school.notificationservice.config.email.EmailSenderConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmailNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class EmailServiceTest {

    private EmailSenderConfig senderConfig;

    @Mock
    private JavaMailSenderImpl mailSender;

    @InjectMocks
    private EmailService emailService;

    private static final UserDto USER = UserDto.builder()
            .id(1L)
            .username("user")
            .email("test@example.com")
            .phone("9999999")
            .aboutMe( "testabout")
            .preference(UserDto.PreferredContact.EMAIL)
            .locale(Locale.ENGLISH)
            .build();

    @BeforeEach
    void setUp() {
        senderConfig = new EmailSenderConfig("receiver@example.com", "Hello!");
        emailService = new EmailService(mailSender, senderConfig);
    }

    @Test
    void positive_send_successful() {
        emailService.send(USER, "Hello!");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void negative_when_email_is_blank() {
        USER.setEmail("");
        assertThrows(EmailNotValidException.class, () -> emailService.send(USER, "Hello!"));
    }

    @Test
    void negative_when_email_is_null() {
        USER.setEmail(null);
        assertThrows(EmailNotValidException.class, () -> emailService.send(USER, "Hello!"));
    }

}