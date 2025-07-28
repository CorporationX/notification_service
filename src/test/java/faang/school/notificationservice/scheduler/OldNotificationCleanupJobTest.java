package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OldNotificationsCleanupJobTest {

    @InjectMocks
    private OldNotificationsCleanupJob cleanupJob;

    @Mock
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() throws Exception {
        setField(cleanupJob);
    }

    @Test
    void shouldCleanOldNotificationsSuccessfully() {
        cleanupJob.cleanOldNotifications();

        verify(notificationRepository, times(1)).deleteOldNotifications(any(LocalDateTime.class));
    }

    @Test
    void shouldHandleExceptionDuringCleanup() {
        doThrow(new RuntimeException("Database error"))
                .when(notificationRepository).deleteOldNotifications(any());

        cleanupJob.cleanOldNotifications();

        verify(notificationRepository, times(1)).deleteOldNotifications(any(LocalDateTime.class));
    }

    private void setField(Object target) throws Exception {
        Field field = target.getClass().getDeclaredField("cleanupInterval");
        field.setAccessible(true);
        field.set(target, 30);
    }
}