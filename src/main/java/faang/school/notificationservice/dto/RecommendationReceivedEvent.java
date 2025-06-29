package faang.school.notificationservice.dto;

import java.time.LocalDateTime;

public record RecommendationReceivedEvent(
        Long recommendationId,
        Long authorId,
        Long receiverId,
        String content,
        LocalDateTime createdAt) {
}
