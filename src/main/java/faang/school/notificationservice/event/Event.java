package faang.school.notificationservice.event;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public abstract class Event {
    private UUID eventId;
    private Instant occurredAt;
    private String eventType;
    private String source;
    private String traceId;
    private Long authorId;
    private Long userId;
    private Long receiverId;
}
