package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationAggregationService {

    private record NotificationGroupKey(Long receiverId, Long targetEntityId, EventType eventType) {
    }

    public List<AggregatedNotificationsDto> aggregateNotifications(List<PendingNotifications> notifications) {
        Map<NotificationGroupKey, List<PendingNotifications>> groupedNotifications = notifications.stream()
                .collect(Collectors.groupingBy(
                        this::createGroupKey,
                        Collectors.toList()
                ));

        return groupedNotifications.entrySet().stream()
                .map(this::mapToAggregatedNotificationsDto)
                .toList();
    }

    private NotificationGroupKey createGroupKey(PendingNotifications notification) {
        return new NotificationGroupKey(
                notification.getReceiverId(),
                notification.getTargetEntityId(),
                notification.getEventType()
        );
    }

    private AggregatedNotificationsDto mapToAggregatedNotificationsDto(Map.Entry<NotificationGroupKey, List<PendingNotifications>> entry) {
        NotificationGroupKey key = entry.getKey();
        List<PendingNotifications> group = entry.getValue();

        PendingNotifications firstNotification = group.get(0);

        return AggregatedNotificationsDto.builder()
                .receiverId(key.receiverId())
                .targetEntityId(key.targetEntityId())
                .eventData(firstNotification.getEventData())
                .eventType(key.eventType())
                .notificationCount(group.size())
                .build();
    }
}