package faang.school.notificationservice.listener.event;

import lombok.Data;

@Data
public class CommentEvent {
    private Long authorCommentId;
    private Long authorPostId;
    private Long postId;
    private String content;
    private Long commentId;

}
