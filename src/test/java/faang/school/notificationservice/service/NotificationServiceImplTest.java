package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testSendEmail() throws Exception {
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setPreference(UserDto.PreferredContact.EMAIL);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.send(user, "Test message");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendSms() {
        UserDto user = new UserDto();
        user.setPhone("1234567890");
        user.setPreference(UserDto.PreferredContact.SMS);

        notificationService.send(user, "Test message");

        verify(smsService, times(1)).sendSms(eq("1234567890"), eq("Test message"));
    }

    @Test
    void testSendUnknownContactMethod() {
        UserDto user = new UserDto();
        user.setPreference(null);

        notificationService.send(user, "Test message");

        verify(mailSender, never()).send(any(MimeMessage.class));
        verify(smsService, never()).sendSms(anyString(), anyString());
    }
}