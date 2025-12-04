package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RecommendationReceiveEvent(
        @NotNull(message = "Recommendation ID cannot be null")
        Long recommendationId,
        @NotNull(message = "Author recommendation ID cannot be null")
        Long authorId,
        @NotNull(message = "Receiver recommendation ID cannot be null")
        Long receiverId,
        LocalDateTime recommendationDate
) {
}