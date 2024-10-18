package faang.school.notificationservice.dto.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LikeEvent {
    private final Long likeAuthorId;
    private final Long postId;
    private final Long postAuthorId;
    private final LocalDateTime createdAt;
}
