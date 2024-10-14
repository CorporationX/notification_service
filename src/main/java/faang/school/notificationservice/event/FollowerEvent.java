package faang.school.notificationservice.event;

import lombok.Builder;

@Builder
public record FollowerEvent(
        long followerId,
        long followedId,
        String followerName
) {
}