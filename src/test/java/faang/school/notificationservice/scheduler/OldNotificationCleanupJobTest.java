package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.service.notification.NotificationCleanupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OldNotificationCleanupJobTest {

    @InjectMocks
    private OldNotificationsCleanupJob cleanupJob;

    @Mock
    private NotificationCleanupService notificationCleanupService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldTriggerOldNotificationsCleanup() {
        cleanupJob.triggerOldNotificationsCleanup();

        verify(notificationCleanupService, times(1)).cleanOldNotifications();
    }
}