package faang.school.notificationservice.event;

import lombok.Builder;

@Builder
public record SkillAcquiredEvent(
        long userId,
        long skillId
) {
}
