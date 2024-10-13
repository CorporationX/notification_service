package faang.school.notificationservice.event;

import lombok.Builder;

@Builder
public record MentorshipOfferedEvent(
        Long requestId,
        Long requesterId,
        Long receiverId
) {
}
