package faang.school.notificationservice.dto.skill;

import lombok.Builder;

@Builder
public record SkillAcquiredEvent(long userId, long skillId) {
}
