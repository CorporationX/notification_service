package faang.school.notificationservice.event;

import lombok.Builder;

@Builder
public record RecommendationEvent(
        Long requesterId,
        Long receiverId,
        Long recommendationId) {
}
