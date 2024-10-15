package faang.school.notificationservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikePostEvent {
    private Long likeAuthorId;
    private Long postId;
    private Long postAuthorId;
}
