package faang.school.notificationservice.event;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EventStartEvent (Long eventId, String eventTitle, LocalDateTime eventStartTime, List<Long> attendeesIds) {
}
