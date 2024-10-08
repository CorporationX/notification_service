package faang.school.notificationservice.dto;

public record GoalCompletedEventDto(
        long userId,
        long goalId) {
}