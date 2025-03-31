package faang.school.notificationservice.dto.kafka;

import java.time.LocalDateTime;

public record FollowUserDto (
    Long followerId,
    Long followeeId,
    LocalDateTime followedAt
){}
