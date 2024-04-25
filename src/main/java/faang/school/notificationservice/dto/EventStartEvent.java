package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EventStartEvent {

    @NotNull
    long event_id;

    @NotNull
    List<Long> attendeeIds;

}
