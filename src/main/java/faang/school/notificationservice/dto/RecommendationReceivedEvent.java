package faang.school.notificationservice.dto;

public record RecommendationReceivedEvent(
        Long recommendationId,
        Long authorId,
        Long recipientId
) {
}