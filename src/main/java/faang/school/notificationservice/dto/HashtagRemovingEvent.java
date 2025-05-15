package faang.school.notificationservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HashtagRemovingEvent(
        Long userId,
        String hashtagName,
        LocalDateTime removedAt
) {
}
