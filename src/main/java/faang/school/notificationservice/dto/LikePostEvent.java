package faang.school.notificationservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LikePostEvent {
    private long postId;
    private long postAuthorId;
    private long likeUserId;
}
