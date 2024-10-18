package faang.school.notificationservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent {
    private Long authorId;
    private Long postAuthorId;
    private Long postId;
    private String postText;
    private Long commentId;
}
