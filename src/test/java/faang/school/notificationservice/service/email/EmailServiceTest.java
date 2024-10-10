package faang.school.notificationservice.service.email;

import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService service;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MessageSource messageSource;

    private UserDto user;
    private String text;
    private String subjectCode;
    private String subject;
    private Locale locale;

    @BeforeEach
    public void setUp() {
        user = new UserDto();
        user.setEmail("test@test.com");
        text = "Test email body";
        subjectCode = "email.subject";
        subject = "Test Subject";
        locale = Locale.ENGLISH;
    }

    @Test
    public void testGetPreferredContact() {
        UserDto.PreferredContact preferredContact = service.getPreferredContact();

        assertEquals(UserDto.PreferredContact.EMAIL, preferredContact);
    }

    @Test
    public void testSendEmail_Success() {
        when(messageSource.getMessage(subjectCode, null, locale)).thenReturn(subject);

        service.send(user, text, subjectCode, locale);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailWithMailException() {
        when(messageSource.getMessage(subjectCode, null, locale)).thenReturn(subject);

        doThrow(new MailException("Mail exception") {
        }).when(mailSender).send(any(SimpleMailMessage.class));

        service.send(user, text, subjectCode, locale);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailWithUnexpectedException() {
        when(messageSource.getMessage(subjectCode, null, locale)).thenReturn(subject);

        doThrow(new RuntimeException("Unexpected exception")).when(mailSender).send(any(SimpleMailMessage.class));

        service.send(user, text, subjectCode, locale);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailWithDefaultLocale() {
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn(subject);

        service.send(user, text);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
