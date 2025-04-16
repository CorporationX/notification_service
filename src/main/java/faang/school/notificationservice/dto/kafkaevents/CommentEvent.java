package faang.school.notificationservice.dto.kafkaevents;

import java.time.LocalDateTime;

public record CommentEvent(
        long id,
        long commentAuthorId,
        long postAuthorId,
        long postId,
        String content,
        LocalDateTime createdAt
) {
}
