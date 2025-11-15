package faang.school.notificationservice.event.mentorship;

import lombok.Builder;

@Builder
public record MentorshipOfferedEvent(
        Long mentorshipRequestId,
        Long mentorId,
        Long menteeId
) {
}