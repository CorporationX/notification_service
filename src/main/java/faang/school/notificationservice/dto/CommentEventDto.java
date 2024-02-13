package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEventDto {
    private long postId;
    private long authorCommentId;
    private long authorPostId;
    private long commentId;
    private String content;
}
