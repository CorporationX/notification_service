package faang.school.notificationservice.dto;

import java.util.List;

public record EventStartEventDto(
        Long eventId,
        Long userId,
        List<Long> attendeesIds,
        String title,
        TimeLeft timeLeft
) {
}
