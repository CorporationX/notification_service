package faang.school.notificationservice.dto;

import java.time.LocalDateTime;

public record RecommendationReceivedEvent(
        long authorId,
        long receiverId,
        String message,
        LocalDateTime recommendationTime) {
}
