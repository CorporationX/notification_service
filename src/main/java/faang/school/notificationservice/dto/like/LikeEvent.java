package faang.school.notificationservice.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeEvent {

    private Long authorId;
    private Long postId;
    private Long userId;
    private LocalDateTime likeTime;
}
