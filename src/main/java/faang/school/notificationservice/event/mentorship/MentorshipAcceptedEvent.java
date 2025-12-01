package faang.school.notificationservice.event.mentorship;

import lombok.Builder;

@Builder
public record MentorshipAcceptedEvent(
        Long mentorshipRequestId,
        Long mentorId,
        Long menteeId
) {
}