package faang.school.notificationservice.dto.publisher_events;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendationReceivedEvent {
    private Long id;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime timestamp;
}
