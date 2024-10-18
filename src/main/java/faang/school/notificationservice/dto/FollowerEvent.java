package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record FollowerEvent(
    Long followerId,
    Long followeeId,
    LocalDateTime timestamp
) {}
