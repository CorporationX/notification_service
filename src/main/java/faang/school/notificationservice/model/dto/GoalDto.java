package faang.school.notificationservice.model.dto;

import lombok.Builder;

@Builder
public record GoalDto(
        String title,
        String description
) {
}
