package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationSenderTest {

    @Mock
    private UserServiceClient userServiceClient;

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
        notificationSender = new NotificationSender(services, userServiceClient);
    }

    @Test
    void sendNotificationSuccessTest() {
        when(userServiceClient.getUser(USER_ID)).thenReturn(testUser);
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        notificationSender.sendNotification(USER_ID, TEST_MESSAGE);

        verify(emailNotificationService).send(testUser, TEST_MESSAGE);
        verify(telegramNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotificationUserNotFoundFailTest() {
        when(userServiceClient.getUser(USER_ID))
                .thenThrow(mock(FeignException.class));

        assertThrows(EntityNotFoundException.class,
                () -> notificationSender.sendNotification(USER_ID, TEST_MESSAGE));

        verify(emailNotificationService, never()).send(any(), any());
        verify(telegramNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotificationUnsupportedPreferenceFailTest() {
        testUser.setPreference(UserDto.PreferredContact.SMS);
        when(userServiceClient.getUser(USER_ID)).thenReturn(testUser);

        assertThrows(IllegalArgumentException.class,
                () -> notificationSender.sendNotification(USER_ID, TEST_MESSAGE));

        verify(emailNotificationService, never()).send(any(), any());
        verify(telegramNotificationService, never()).send(any(), any());
    }

    @Test
    void sendNotificationTelegramPreferenceSuccessTest() {
        testUser.setPreference(UserDto.PreferredContact.TELEGRAM);
        when(userServiceClient.getUser(USER_ID)).thenReturn(testUser);
        when(telegramNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        notificationSender.sendNotification(USER_ID, TEST_MESSAGE);

        verify(telegramNotificationService).send(testUser, TEST_MESSAGE);
        verify(emailNotificationService, never()).send(any(), any());
    }

    @Test
    void getUserDtoSuccessTest() {
        when(userServiceClient.getUser(USER_ID)).thenReturn(testUser);

        UserDto result = notificationSender.getUserDto(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        assertEquals(UserDto.PreferredContact.EMAIL, result.getPreference());
    }
}