package faang.school.notificationservice.model.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FollowerEvent(
        long followerId,
        long followeeId,
        LocalDateTime subscribedAt
) {
}
