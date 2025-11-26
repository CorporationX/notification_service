package faang.school.notificationservice.dto.events;

import lombok.Builder;

@Builder
public record NewFollowerEventDto(
        long actorId,
        long receiverId,
        String followerDisplayName
) {
}