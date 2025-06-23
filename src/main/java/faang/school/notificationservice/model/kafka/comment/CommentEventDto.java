package faang.school.notificationservice.model.kafka.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEventDto {
    private long id;
    private long postId;
    private long authorId;
    private long authorPostId;
}
