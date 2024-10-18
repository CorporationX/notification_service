package faang.school.notificationservice.model.event;

import lombok.Builder;

@Builder
public record SkillOfferedEvent(
        long receiverId,
        long senderId,
        long skillId
) {
}
