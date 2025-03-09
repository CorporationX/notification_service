package faang.school.notificationservice.dto;


import lombok.Builder;

@Builder
public record RecommendationRequestedEvent(
        long requestAuthorId,
        long targetUserId,
        long recommendationRequestId) {
}
