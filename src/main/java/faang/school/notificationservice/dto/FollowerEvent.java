package faang.school.notificationservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Locale;

@Builder
public record FollowerEvent(
        long followerId,
        long followeeId,
        LocalDateTime followTime,
        Locale locale
) {
}
