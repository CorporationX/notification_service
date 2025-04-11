package faang.school.notificationservice.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEvent {

    private Long authorPostId;
    private Long authorLikeId;
    private Long postId;
    private LocalDateTime createdAt;
}
