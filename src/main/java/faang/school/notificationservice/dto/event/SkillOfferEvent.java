package faang.school.notificationservice.dto.event;

import lombok.Builder;

@Builder
public record SkillOfferEvent(
        long id,
        long requesterId,
        Long receiverId,
        String skillTitle
) {
}
