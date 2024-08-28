package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EventStartEvent {
    private Long eventId;
    private List<Long> participants;
}
