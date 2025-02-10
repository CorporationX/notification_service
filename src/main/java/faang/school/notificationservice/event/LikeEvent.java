package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeEvent {
    private Long postId;
    private Long userId;
    private Long authorId;
}