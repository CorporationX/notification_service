package faang.school.notificationservice.dto.skill;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillAcquiredEvent {
    private Long receiverId;
    private Long skillId;
    private String skillTitle;
}
