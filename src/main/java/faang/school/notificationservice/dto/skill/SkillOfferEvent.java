package faang.school.notificationservice.dto.skill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillOfferEvent {
    private Long senderId;
    private Long receiverId;
    private Long skillId;
}
