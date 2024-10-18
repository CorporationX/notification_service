package faang.school.notificationservice.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewCommentEvent {
    private long commentAuthorId;
    private long postAuthorId;
    private long postId;
    private String content;
    private long commentId;
}
