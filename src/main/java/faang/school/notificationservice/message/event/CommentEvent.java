package faang.school.notificationservice.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent extends NotificationEvent {
    private long commentId;
    private long commentAuthorId;
    private long postId;
}
