package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;
import faang.school.notificationservice.service.notification.EventType;
import faang.school.notificationservice.service.notification.NotificationAggregationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationAggregationServiceTest {

    private final NotificationAggregationService aggregationService =
            new NotificationAggregationService();

    @Test
    void testNotificationsAggregatedCorrectly() {
        PendingNotifications notification1 = PendingNotifications.builder()
                .receiverId(42L)
                .targetEntityId(123L)
                .relatedEntityId(1L)
                .eventType(EventType.COMMENT_LIKED)
                .build();

        PendingNotifications notification2 = PendingNotifications.builder()
                .receiverId(42L)
                .targetEntityId(123L)
                .relatedEntityId(2L)
                .eventType(EventType.COMMENT_LIKED)
                .build();

        PendingNotifications notification3 = PendingNotifications.builder()
                .receiverId(42L)
                .targetEntityId(456L)
                .relatedEntityId(3L)
                .eventType(EventType.POST_LIKED)
                .build();

        List<PendingNotifications> inputNotifications = List.of(
                notification1,
                notification2,
                notification3
        );

        List<AggregatedNotificationsDto> aggregated = aggregationService
                .aggregateNotifications(inputNotifications);

        assertEquals(2, aggregated.size());

        AggregatedNotificationsDto group1 = aggregated.stream()
                .filter(dto -> dto.getTargetEntityId().equals(123L))
                .findFirst()
                .orElseThrow();
        assertEquals(42L, group1.getReceiverId());
        assertEquals(123L, group1.getTargetEntityId());
        assertEquals(EventType.COMMENT_LIKED, group1.getEventType());
        assertEquals(2, group1.getNotificationCount());
        assertEquals(1L, group1.getRelatedEntityId());

        AggregatedNotificationsDto group2 = aggregated.stream()
                .filter(dto -> dto.getTargetEntityId().equals(456L))
                .findFirst()
                .orElseThrow();
        assertEquals(42L, group2.getReceiverId());
        assertEquals(456L, group2.getTargetEntityId());
        assertEquals(EventType.POST_LIKED, group2.getEventType());
        assertEquals(1, group2.getNotificationCount());
        assertEquals(3L, group2.getRelatedEntityId());
    }
}