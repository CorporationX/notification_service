package faang.school.notificationservice.scheduler;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.NotificationAggregationService;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationSchedulerJobTest {

    @InjectMocks
    private NotificationSchedulerJob notificationSchedulerJob;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationSenderService notificationSender;

    @Mock
    private NotificationAggregationService aggregator;

    @BeforeEach
    void setUp() throws Exception {
        setField(notificationSchedulerJob, "delayHours", 3);
        setField(notificationSchedulerJob, "lastSentThresholdHours", 1);
        setField(notificationSchedulerJob, "totalInstances", 1);
        setField(notificationSchedulerJob, "currentInstance", 1);
    }

    @Test
    void shouldNotSendIfNoPendingNotifications() {
        when(notificationRepository.findAndLockPendingNotifications(any(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        notificationSchedulerJob.publishNotifications();

        verify(notificationRepository, times(1))
                .findAndLockPendingNotifications(any(), any(), anyInt(), anyInt());
        verifyNoInteractions(aggregator, notificationSender);
    }

    @Test
    void shouldSendNotificationsIfPendingNotificationsExist() {
        PendingNotifications pendingNotification = new PendingNotifications();
        List<PendingNotifications> pendingNotifications = List.of(pendingNotification);
        AggregatedNotificationsDto aggregatedNotification = new AggregatedNotificationsDto();
        List<AggregatedNotificationsDto> aggregatedNotifications = List.of(aggregatedNotification);

        when(notificationRepository.findAndLockPendingNotifications(any(), any(), anyInt(), anyInt()))
                .thenReturn(pendingNotifications);
        when(aggregator.aggregateNotifications(pendingNotifications))
                .thenReturn(aggregatedNotifications);

        notificationSchedulerJob.publishNotifications();

        verify(notificationRepository, times(1))
                .findAndLockPendingNotifications(any(), any(), anyInt(), anyInt());
        verify(aggregator, times(1))
                .aggregateNotifications(pendingNotifications);
        verify(notificationSender, times(1))
                .sendAggregatedNotifications(aggregatedNotification);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}