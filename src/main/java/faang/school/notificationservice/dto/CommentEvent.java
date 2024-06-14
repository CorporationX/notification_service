package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentEvent {
    private long commentAuthorId;
    private long postAuthorId;
    private long postId;
    private long commentId;
    private String commentText;
}
