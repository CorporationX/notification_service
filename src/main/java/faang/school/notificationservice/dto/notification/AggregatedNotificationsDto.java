package faang.school.notificationservice.dto.notification;

import faang.school.notificationservice.service.notification.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AggregatedNotificationsDto {
    private Long receiverId;
    private Long targetEntityId;
    private Long relatedEntityId;
    private EventType eventType;
    private Long notificationCount;
}