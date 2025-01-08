package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalCompletedEvent {
    private long userId;
    private long goalId;
    private LocalDateTime goalAchieveDateTime;
}
