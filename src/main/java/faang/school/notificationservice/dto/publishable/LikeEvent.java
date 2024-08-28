package faang.school.notificationservice.dto.publishable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeEvent {
    private long actorId;
    private long receiverId;
    private long postId;
}
