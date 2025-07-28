package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationAggregationService {

    private record NotificationGroupKey(Long receiverId, Long targetEntityId, EventType eventType) {
    }

    public List<AggregatedNotificationsDto> aggregateNotifications(List<PendingNotifications> notifications) {
        Map<NotificationGroupKey, LongSummaryStatistics> groupedStats = notifications.stream()
                .collect(Collectors.groupingBy(
                        this::createGroupKey,
                        Collectors.summarizingLong(PendingNotifications::getRelatedEntityId)
                ));

        return groupedStats.entrySet().stream()
                .map(this::mapToAggregatedNotificationsDto) // <-- И здесь стало намного чище
                .toList();
    }

    private NotificationGroupKey createGroupKey(PendingNotifications notification) {
        return new NotificationGroupKey(
                notification.getReceiverId(),
                notification.getTargetEntityId(),
                notification.getEventType()
        );
    }

    private AggregatedNotificationsDto mapToAggregatedNotificationsDto(Map.Entry<NotificationGroupKey, LongSummaryStatistics> entry) {
        NotificationGroupKey key = entry.getKey();
        LongSummaryStatistics stats = entry.getValue();

        return AggregatedNotificationsDto.builder()
                .receiverId(key.receiverId())
                .targetEntityId(key.targetEntityId())
                .eventType(key.eventType())
                .notificationCount(stats.getCount())
                .relatedEntityId(stats.getMin())
                .build();
    }
}