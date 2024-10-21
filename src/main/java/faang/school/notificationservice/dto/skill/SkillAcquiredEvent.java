package faang.school.notificationservice.dto.skill;

import lombok.Getter;

@Getter
public class SkillAcquiredEvent {

    private Long receiverId;
    private Long skillId;
    private String skillTitle;
}
