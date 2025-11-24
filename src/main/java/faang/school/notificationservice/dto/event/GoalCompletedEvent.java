package faang.school.notificationservice.dto.event;

import jakarta.validation.constraints.NotNull;

public record GoalCompletedEvent(
        @NotNull
        Long userId,
        @NotNull
        Long goalId
) {
}