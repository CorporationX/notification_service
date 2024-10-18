package faang.school.notificationservice.dto.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LikeEvent {
    private Long authorLikeId;
    private Long authorPostId;
    private Long postId;
    private LocalDateTime createdAt;
}
