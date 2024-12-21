package faang.school.notificationservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import faang.school.notificationservice.deserializer.LocalDateTimeArrayDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCompletedEventDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long goalId;

    @JsonDeserialize(using = LocalDateTimeArrayDeserializer.class)
    private LocalDateTime completedAt;
}
