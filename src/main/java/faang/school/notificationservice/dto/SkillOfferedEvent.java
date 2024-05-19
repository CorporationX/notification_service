package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillOfferedEvent {
    private long recipientUserId;
    private long senderUserId;
    private long skillId;
    private String titleSkill;
}