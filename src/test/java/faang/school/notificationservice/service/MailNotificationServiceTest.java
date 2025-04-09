package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.impl.MailNotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailNotificationServiceTest {

    @Mock
    private MailService emailService;

    @InjectMocks
    private MailNotificationService mailNotificationService;

    @Test
    void testSendEmailWhenUserPrefersEmail() {
        UserDto user = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        String message = "Test message";

        mailNotificationService.send(user, message);

        verify(emailService, times(1))
                .sendEmail(user.getEmail(), "Griffon team - sending mail", message);
    }

    @Test
    void testSendEmailWhenUserDoesNotPreferEmail() {
        UserDto user = UserDto.builder()
                .id(1L)
                .email("test@example.com")
                .preference(UserDto.PreferredContact.PHONE)
                .build();

        mailNotificationService.send(user, "Test message");

        verify(emailService, never())
                .sendEmail(any(), any(), any());
    }

    @ParameterizedTest
    @CsvSource({
            "1, null",
            "2, ''"
    })
    void testSendEmailWhenEmailInvalid(long userId, String email) {
        UserDto user = UserDto.builder()
                .id(userId)
                .email("null".equals(email) ? null : email)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        mailNotificationService.send(user, "Test message");

        verify(emailService, never()).sendEmail(any(), any(), any());
    }
}
