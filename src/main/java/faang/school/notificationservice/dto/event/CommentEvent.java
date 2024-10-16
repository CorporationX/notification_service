package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent {
    private long commentAuthorId;
    private long postAuthorId;
    private long postId;
    private long commentId;
    private String comment;
}
