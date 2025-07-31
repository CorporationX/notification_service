package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private Long commentId;
    private Long postId;
    private Long postAuthorId;
    private Long commentAuthorId;
    private String commentText;
}
