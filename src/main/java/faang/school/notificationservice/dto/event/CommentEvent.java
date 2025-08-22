package faang.school.notificationservice.dto.event;

import lombok.Builder;

@Builder
public record CommentEvent(
        long id,
        long postAuthorId,
        long commentAuthorId,
        long postId,
        String content
) {}
