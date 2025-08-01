package faang.school.notificationservice.dto.event;

import java.io.Serializable;

public record CommentEvent(
        Long postId,
        Long commentId,
        Long commentAuthorId,
        Long postAuthorId,
        String commentText
) implements Serializable {
}
