package faang.school.notificationservice.model.event;

import lombok.Builder;

@Builder
public record ProjectFollowerEvent(
        long followerId,
        long projectId,
        long ownerId
) {
}