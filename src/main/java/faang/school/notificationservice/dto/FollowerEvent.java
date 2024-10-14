package faang.school.notificationservice.dto;

public record FollowerEvent(
    Long followerId,
    Long followeeId
) {}
