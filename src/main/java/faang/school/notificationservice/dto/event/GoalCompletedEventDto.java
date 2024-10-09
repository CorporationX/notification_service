package faang.school.notificationservice.dto.event;

public record GoalCompletedEventDto(
        long userId,
        long eventId,
        String timestamp) {
}