package faang.school.notificationservice.dto;

import java.time.LocalDateTime;

public record PostPublishedEvent(
        Long postId,
        Long authorId,
        String content,
        LocalDateTime publishedAt
) {
}
