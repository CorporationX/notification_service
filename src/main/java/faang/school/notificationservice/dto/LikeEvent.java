package faang.school.notificationservice.dto;

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
    private Long postId;
    private Long postAuthorId;
    private Long likeUserId;
    private LocalDateTime dateTime;
}