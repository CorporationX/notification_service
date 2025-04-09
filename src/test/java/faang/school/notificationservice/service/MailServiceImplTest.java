package faang.school.notificationservice.service;

import faang.school.notificationservice.config.email.MailProperties;
import faang.school.notificationservice.exception.EmailSendingException;
import faang.school.notificationservice.service.impl.MailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailProperties mailProperties;

    @InjectMocks
    private MailServiceImpl mailService;

    @BeforeEach
    void setUp() {
        mailProperties = new MailProperties();
        mailProperties.setFrom("noreply@griffon.com");
        mailProperties.setReplyTo("reply@griffon.com");
        mailService = new MailServiceImpl(javaMailSender, mailProperties);
    }

    @Test
    void sendEmailWhenEmailIsValid() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService.sendEmail(to, subject, text);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendEmailWhenEmailIsInvalid() {
        String to = "invalid-email";
        String subject = "Test Subject";
        String text = "Test Text";

        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> {
                    mailService.sendEmail(to, subject, text);
                });

        assertEquals("Invalid email address: " + to, thrown.getMessage());
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendEmailWhenMailExceptionOccurs() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailException("Mail sending failed") {}).when(javaMailSender).send(any(MimeMessage.class));

        EmailSendingException thrown = assertThrows(EmailSendingException.class, () -> {
            mailService.sendEmail(to, subject, text);
        });

        assertEquals("Email send failed", thrown.getMessage());
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
}
