package faang.school.notificationservice.listener;

import lombok.Data;

@Data
public class LikePostEvent {

    private Long postAuthorId;
    private Long postId;
    private Long actionUserId;
}
