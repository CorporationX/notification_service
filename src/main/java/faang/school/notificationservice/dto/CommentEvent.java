package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class CommentEvent {
    private long commentAuthorId;
    private long postAuthorId;
    private long commentId;
    private long postId;
    private String content;
}