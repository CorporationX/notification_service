package faang.school.notificationservice.model.event;

import lombok.Builder;

@Builder
public record LikeEvent(long postAuthorId,
                        long likeAuthorId,
                        long postId) {
}
