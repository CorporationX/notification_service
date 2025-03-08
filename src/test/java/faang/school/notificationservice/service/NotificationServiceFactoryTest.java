package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserProfileDto;
import faang.school.notificationservice.service.email.EmailNotificationService;
import faang.school.notificationservice.service.telegram.TelegramNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceFactoryTest {

    private NotificationServiceFactory factory;

    @Mock
    private EmailNotificationService emailService;
    @Mock
    private TelegramNotificationService telegramService;

    @BeforeEach
    void setUp() {
        when(emailService.getPreferredContact()).thenReturn(UserProfileDto.PreferredContact.EMAIL);
        when(telegramService.getPreferredContact()).thenReturn(UserProfileDto.PreferredContact.TELEGRAM);

        factory = new NotificationServiceFactory(List.of(emailService, telegramService));
    }

    @Test
    void testGetEmailService() {
        NotificationService service = factory.getService(UserProfileDto.PreferredContact.EMAIL);
        assertEquals(emailService, service);
    }

    @Test
    void testGetTelegramService() {
        NotificationService service = factory.getService(UserProfileDto.PreferredContact.TELEGRAM);
        assertEquals(telegramService, service);
    }

    @Test
    void testUnknownPreference() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.getService(UserProfileDto.PreferredContact.PHONE));
    }
}