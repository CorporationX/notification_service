package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent {
    private long id;
    private long postId;
    private long authorId;
    // TODO: возможно пост создал проект, а не пользователь
    private long authorPostId;
}
