package faang.school.notificationservice.dto;

import java.util.List;

public record EventStartEvent(
        long eventId,
        List<Long> participantsIds
) {
}
