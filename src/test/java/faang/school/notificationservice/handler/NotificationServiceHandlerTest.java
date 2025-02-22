package faang.school.notificationservice.handler;

import faang.school.notificationservice.dto.UserServiceDto;
import faang.school.notificationservice.exception.impl.non_retryable.ListSizeNotOneException;
import faang.school.notificationservice.exception.impl.non_retryable.NotFoundElementException;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceHandlerTest {

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService smsNotificationService;

    private NotificationServiceHandler notificationServiceHandler;

    private UserServiceDto userWithEmail;
    private UserServiceDto userWithSms;
    private String message;

    @BeforeEach
    void setUp() {
        userWithEmail = UserServiceDto.builder().preference(UserServiceDto.PreferredContact.EMAIL).build();
        userWithSms = UserServiceDto.builder().preference(UserServiceDto.PreferredContact.SMS).build();
        message = "Test message";

        when(emailNotificationService.getPreferredContact()).thenReturn(UserServiceDto.PreferredContact.EMAIL);
        when(smsNotificationService.getPreferredContact()).thenReturn(UserServiceDto.PreferredContact.SMS);

        notificationServiceHandler = new NotificationServiceHandler(List.of(emailNotificationService, smsNotificationService));
    }

    @Test
    void sendSingleNotificationSendsNotification() {
        notificationServiceHandler.sendSingleNotification(userWithEmail, message);

        verify(emailNotificationService).send(userWithEmail, message);
    }

    @Test
    void sendListOfNotificationMultipleUsersSendsNotificationsToAll() {
        List<UserServiceDto> users = List.of(userWithEmail, userWithSms);

        notificationServiceHandler.sendListOfNotification(users, message);

        verify(emailNotificationService).send(userWithEmail, message);
        verify(smsNotificationService).send(userWithSms, message);
    }

    @Test
    void getNotificationServiceNoServiceFoundThrowsNotFoundElementException() {
        UserServiceDto userWithUnknownPreference = new UserServiceDto();
        userWithUnknownPreference.setPreference(null);

        NotificationServiceHandler handler = new NotificationServiceHandler(List.of(emailNotificationService, smsNotificationService));

        assertThrows(NotFoundElementException.class, () -> handler.sendSingleNotification(userWithUnknownPreference, message));
    }

    @Test
    void getNotificationServiceMultipleServicesFoundThrowsListSizeNotOneException() {
        NotificationService duplicateEmailService = mock(NotificationService.class);
        when(duplicateEmailService.getPreferredContact()).thenReturn(UserServiceDto.PreferredContact.EMAIL);

        NotificationServiceHandler handler = new NotificationServiceHandler(List.of(emailNotificationService, duplicateEmailService, smsNotificationService));

        assertThrows(ListSizeNotOneException.class, () -> handler.sendSingleNotification(userWithEmail, message));
    }
}