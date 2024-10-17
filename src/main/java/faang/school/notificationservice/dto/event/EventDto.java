package faang.school.notificationservice.dto.event;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        List<Long> userIds,
        long eventId,
        String title,
        LocalDateTime startDateTime) {
}