package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoalCompletedEvent {
    private Long completedGoalId;
}
