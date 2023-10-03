package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentEventDto {
    private Long commentId;
    private Long authorCommentId;
    private Long authorPostId;
    private String content;
}
