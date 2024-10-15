package faang.school.notificationservice.model.event;

import lombok.Builder;

@Builder
public record CommentEvent(
        long commentAuthorId,
        String username,
        long postAuthorId,
        long postId,
        String content,
        long commentId
) {
}