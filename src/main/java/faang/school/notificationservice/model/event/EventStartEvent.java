package faang.school.notificationservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventStartEvent {

    @NotNull
    Long userId;
    @NotNull
    Long eventId;
    @NotNull
    List<Long> participantIds;
    @NotNull
    String title;
    @NotNull
    LocalDateTime startDateTime;
}
