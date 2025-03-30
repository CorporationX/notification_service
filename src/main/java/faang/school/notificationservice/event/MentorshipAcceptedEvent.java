package faang.school.notificationservice.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MentorshipAcceptedEvent(
        @NotNull
        Long requesterId,
        @NotNull
        Long requestId,
        @NotNull
        Long mentorId
) {
}
