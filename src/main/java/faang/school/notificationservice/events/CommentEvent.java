package faang.school.notificationservice.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record CommentEvent(
        Long commentId,
        Long commentAuthorId,
        Long postId,
        Long postAuthorId,
        String commentText,
        LocalDateTime createdAt
) implements Serializable {
    private static final long serialVersionUID = 1L;
}