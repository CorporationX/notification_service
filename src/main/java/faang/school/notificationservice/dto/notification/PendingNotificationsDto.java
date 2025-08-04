package faang.school.notificationservice.dto.notification;

import com.fasterxml.jackson.databind.JsonNode;
import faang.school.notificationservice.service.notification.EventType;
import faang.school.notificationservice.service.notification.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingNotificationsDto {
    private Long receiverId;
    private Long targetEntityId;
    private JsonNode eventData;
    private EventType eventType;
    private NotificationStatus status;
}
