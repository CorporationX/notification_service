package faang.school.notificationservice.dto.event;

public record GoalCompletedEvent(
        Long userId,
        Long goalId
) {
}