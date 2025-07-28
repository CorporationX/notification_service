package faang.school.notificationservice.dto.notification;

import faang.school.notificationservice.service.notification.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggregatedNotificationsDto {
    private Long receiverId;
    private Long targetEntityId;
    private Long relatedEntityId;
    private EventType eventType;
    private Long notificationCount;
}