package faang.school.notificationservice.dto.event;

import java.time.LocalDateTime;

public record CommentEvent(long postId, long postAuthorId, long commentId, LocalDateTime date) {
}