package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ServiceUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceResolverTest {

    @Mock
    private NotificationService phoneNotificationService;

    private NotificationServiceResolver resolver;

    @BeforeEach
    void setUp() {
        when(phoneNotificationService.getPreferredContact())
                .thenReturn(UserDto.PreferredContact.PHONE);

        resolver = new NotificationServiceResolver(List.of(phoneNotificationService));
    }

    @Test
    @DisplayName("Should return service matching the preferred contact")
    public void shouldReturnMatchingService() {
        NotificationService result = resolver.getServiceForPreferredContact(UserDto.PreferredContact.PHONE);
        assertEquals(phoneNotificationService, result);

        verify(phoneNotificationService).getPreferredContact();
        verifyNoMoreInteractions(phoneNotificationService);
    }

    @Test
    @DisplayName("Should throw ServiceUnavailableException if no service found for contact")
    public void shouldThrowIfNoServiceFound() {
        assertThrows(ServiceUnavailableException.class,
                () -> resolver.getServiceForPreferredContact(UserDto.PreferredContact.EMAIL)
        );

        verify(phoneNotificationService).getPreferredContact();
        verifyNoMoreInteractions(phoneNotificationService);
    }
}
