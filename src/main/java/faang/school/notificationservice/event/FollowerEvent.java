package faang.school.notificationservice.event;

import java.time.LocalDateTime;

public record FollowerEvent(
        long followeeId,
        long followerId,
        LocalDateTime timestamp
) {}
