package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {

    private long authorId;
    private long postAuthorId;
    private long postId;
    private long commentId;
    private String commentContent;
}
