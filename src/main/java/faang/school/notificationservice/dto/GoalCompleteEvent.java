package faang.school.notificationservice.dto;

public record GoalCompleteEvent(
        Long goalId,
        Long userId
) {
}