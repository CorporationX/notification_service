package faang.school.notificationservice.event;

import lombok.Builder;

@Builder
public record RecommendationReceivedEvent(
        long authorId,
        long receiverId,
        long recommendationId
) {
}
