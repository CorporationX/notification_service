package faang.school.notificationservice.dto.event;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        List<Long> usersId,
        long eventId,
        String title,
        LocalDateTime startDateTime)
{}