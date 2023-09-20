package faang.school.notificationservice.dto.redis;

import faang.school.notificationservice.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationReceivedEvent implements Serializable {
    private EventType eventType;
    private Date receivedAt;
    private Long authorId;
    private Long recipientId;
    private Long recommendationId;
}
