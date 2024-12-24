package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationSenderTest {

    @Mock
    private UserGetter userGetter;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService telegramNotificationService;

    @InjectMocks
    private NotificationSender notificationSender;

    private UserDto testUser;
    private static final Long USER_ID = 1L;
    private static final String TEST_MESSAGE = "Test notification message";

    @BeforeEach
    void setUp() {
        testUser = new UserDto();
        testUser.setId(USER_ID);
        testUser.setPreference(UserDto.PreferredContact.EMAIL);

        List<NotificationService> services = List.of(emailNotificationService, telegramNotificationService);
        notificationSender = new NotificationSender(services, userGetter);
    }

    @Test
    void sendNotificationSuccessTest() {
        when(userGetter.getUserWithValidate(USER_ID)).thenReturn(testUser);
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        notificationSender.sendNotification(USER_ID, TEST_MESSAGE);

        verify(emailNotificationService).send(testUser, TEST_MESSAGE);
        verify(telegramNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotificationUserNotFoundFailTest() {
        when(userGetter.getUserWithValidate(USER_ID))
                .thenThrow(mock(EntityNotFoundException.class));

        assertThrows(EntityNotFoundException.class,
                () -> notificationSender.sendNotification(USER_ID, TEST_MESSAGE));

        verify(emailNotificationService, never()).send(any(), any());
        verify(telegramNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotificationUnsupportedPreferenceFailTest() {
        testUser.setPreference(UserDto.PreferredContact.SMS);
        when(userGetter.getUserWithValidate(USER_ID)).thenReturn(testUser);

        assertThrows(IllegalStateException.class,
                () -> notificationSender.sendNotification(USER_ID, TEST_MESSAGE));

        verify(emailNotificationService, never()).send(any(), any());
        verify(telegramNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotificationTelegramPreferenceSuccessTest() {
        testUser.setPreference(UserDto.PreferredContact.TELEGRAM);
        when(userGetter.getUserWithValidate(USER_ID)).thenReturn(testUser);
        when(telegramNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        notificationSender.sendNotification(USER_ID, TEST_MESSAGE);

        verify(telegramNotificationService).send(testUser, TEST_MESSAGE);
        verify(emailNotificationService, never()).send(any(), any());
    }
}