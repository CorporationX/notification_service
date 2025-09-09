package faang.school.notificationservice.event;

import java.time.LocalDateTime;

public record CommentEvent(
        Long commentId,
        Long commentAuthorId,
        Long postId,
        Long postAuthorId,
        String commentText,
        LocalDateTime createdAt
) {
}