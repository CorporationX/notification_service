package faang.school.notificationservice.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FollowerEventDto(
        long followerId,
        long followeeId,
        LocalDateTime subscribedAt
) {
}
