package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.BusinessException;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.notification.NotificationStrategyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class NotificationStrategyServiceTest {

    @Mock
    List<NotificationService> notificationServices;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    NotificationStrategyService notificationStrategyService;

    @Test
    void testGetNotificationServiceSuccessCase() {
        UserDto userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();

        Mockito.when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        Mockito.when(notificationServices.stream()).thenReturn(Stream.of(notificationService));

        assertDoesNotThrow(() -> notificationStrategyService.getNotificationService(userDto));
    }

    @Test
    void testGetNotificationServiceWithNoServiceMatchesPreference() {
        UserDto userDto = UserDto.builder()
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        Mockito.when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);
        Mockito.when(notificationServices.stream()).thenReturn(Stream.of(notificationService));

        assertThrows(BusinessException.class,
                () -> notificationStrategyService.getNotificationService(userDto));
    }

    @Test
    void testGetNotificationServiceWithNoUserPreference() {
        UserDto userDto = UserDto.builder().build();

        assertThrows(DataValidationException.class,
                () -> notificationStrategyService.getNotificationService(userDto));
    }

}
