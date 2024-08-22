package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventStartEvent {
    private Long eventId;
    private List<Long> participants;
}
