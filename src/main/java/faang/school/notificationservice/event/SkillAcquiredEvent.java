package faang.school.notificationservice.event;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SkillAcquiredEvent {
    private long userId;
    private long skillId;
    private LocalDateTime skillAcquiredDateTime;
}
