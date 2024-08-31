package faang.school.notificationservice.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentEvent {

    private long postId;

    private long postAuthorId;

    private long authorId;

    private long commentId;

    private String commentContent;
    
}
