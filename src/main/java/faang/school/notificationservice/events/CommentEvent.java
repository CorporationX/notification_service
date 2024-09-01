package faang.school.notificationservice.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentEvent implements Notifiable {

    private long postId;

    private long postAuthorId;

    private long authorId;

    private long commentId;

    private String commentContent;

    @Override
    public long getReceiverId() {
        return postAuthorId;
    }
}
