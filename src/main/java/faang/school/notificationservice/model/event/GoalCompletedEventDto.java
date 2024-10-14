package faang.school.notificationservice.model.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GoalCompletedEventDto(
        long userId,
        long goalId,
        LocalDateTime completedAt

) {
}
