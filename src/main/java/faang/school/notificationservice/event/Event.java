package faang.school.notificationservice.event;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class Event {
    private UUID id;
    private LocalDateTime occurredAt;
    private String source;
    private String eventType;
    private Long authorId;
    private Long receiverId;
    private Long userId;
}
