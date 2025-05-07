package faang.school.notificationservice.dto.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentEvent {
    private long postId;
    private long authorId;
    private long commentId;
    private LocalDateTime timestamp;
}