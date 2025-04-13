package faang.school.notificationservice.dto;

import java.time.LocalDateTime;

public record MentorshipAcceptedRequestEvent(
            Long id,
            Long requesterId,
            Long receiverId,
            LocalDateTime createdAt
) {


}

