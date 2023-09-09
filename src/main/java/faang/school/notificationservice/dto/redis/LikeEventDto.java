package faang.school.notificationservice.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeEventDto {
    private long postId;
    private long postAuthor;
    private long commentId;
    private long commentAuthor;
    private long likeAuthor;
    private LocalDateTime dateTime;
}
