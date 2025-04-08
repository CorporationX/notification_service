package faang.school.notificationservice.dto;

import java.time.LocalDateTime;

public record RecommendationReceivedEvent(
        long requesterId,
        long receiverId,
        String recommendationMessage,
        LocalDateTime createdAt) {
}
