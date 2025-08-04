package faang.school.notificationservice.dto.notification;

import com.fasterxml.jackson.databind.JsonNode;
import faang.school.notificationservice.service.notification.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggregatedNotificationsDto {
    private Long receiverId;
    private Long targetEntityId;
    private JsonNode eventData;
    private EventType eventType;
    private int notificationCount;
}