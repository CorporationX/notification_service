package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private long id;
    private long authorId;
    private long postId;
    private long postAuthorId;
    private String content;
}