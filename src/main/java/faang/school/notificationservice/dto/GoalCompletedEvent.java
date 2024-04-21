package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class GoalCompletedEvent {
    private long userId;
    private long goalId;
}
