package faang.school.notificationservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import faang.school.notificationservice.deserializer.LocalDateTimeArrayDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCompletedEventDto {
    private Long userId;
    private Long goalId;
    @JsonDeserialize(using = LocalDateTimeArrayDeserializer.class)
    private LocalDateTime completedAt;
}
