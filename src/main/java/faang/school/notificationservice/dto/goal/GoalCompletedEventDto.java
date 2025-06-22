package faang.school.notificationservice.dto.goal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalCompletedEventDto {
    private Long userId;
    private Long goalId;
}
