package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventStartEventDto {

    @NotNull
    private Long eventId;

    @NotNull
    private List<Long> eventParticipants;

    private EventTimeToStart timeBeforeStart;
}
