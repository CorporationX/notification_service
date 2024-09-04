package faang.school.notificationservice.event.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentEvent {
    private UUID uuid;
    private long commentId;
    private long authorId;
    private long postId;
    private long postAuthorId;
    private String content;
}
