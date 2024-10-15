package faang.school.notificationservice.model.event;

import lombok.Builder;

@Builder
public record MentorshipOfferedEvent(
        Long requestId,
        Long requesterId,
        Long receiverId
) {
}
