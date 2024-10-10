package faang.school.notificationservice.dto.goal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoalCompletedEvent {
    private long goalId;
    private long userId;
    private String goalName;
}
