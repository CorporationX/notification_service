package faang.school.notificationservice.dto.kafkaevents;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentEvent(
        long id,
        long commentAuthorId,
        long postAuthorId,
        long postId,
        String content,
        LocalDateTime createdAt
) {
}
